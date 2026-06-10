package lightOrgan.gateway.serial

import annotations.SkipCoverage
import jsonrpc.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import logging.Logger
import serial.SerialFrameFormat
import serial.SerialPort
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.module.kotlin.jsonMapper
import tools.jackson.module.kotlin.kotlinModule
import tools.jackson.module.kotlin.treeToValue
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

interface SerialJsonRpcConnection : JsonRpcConnection {
    val name: String
    val baudRate: Int
    val frameFormat: SerialFrameFormat
}

// ENHANCEMENT: Add the ability to receive and respond to requests.
// This is a client that communicates over serial based on the JSON-RPC spec.
class RealSerialJsonRpcConnection(
    private val port: SerialPort,
    private val generateId: () -> String = { UUID.randomUUID().toString() },
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : SerialJsonRpcConnection {

    private val pendingRequests = ConcurrentHashMap<String, CompletableDeferred<JsonRpcResponse>>()
    private val jsonMapper = jsonMapper {
        addModule(kotlinModule())
        propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
    }

    override val name: String = port.name
    override val baudRate: Int = port.baudRate
    override val frameFormat: SerialFrameFormat = port.frameFormat

    override val isConnected: StateFlow<Boolean> = port.isOpen

    private val _incomingRequests = MutableSharedFlow<JsonRpcRequest>(extraBufferCapacity = 8)
    override val incomingRequests: Flow<JsonRpcRequest> = _incomingRequests

    private val _incomingNotifications = MutableSharedFlow<JsonRpcNotification>(extraBufferCapacity = 8)
    override val incomingNotifications: Flow<JsonRpcNotification> = _incomingNotifications

    override suspend fun connect() {
        port.open()

        readerJob = scope.launch { readIncomingMessages() }
    }

    override suspend fun disconnect() {
        port.close()
        readerJob?.cancelAndJoin()
        cancelPendingRequests()
    }

    private fun cancelPendingRequests() {
        val exception = kotlin.coroutines.cancellation.CancellationException("Peer disconnected")
        pendingRequests.values.forEach { it.completeExceptionally(exception) }
        pendingRequests.clear()
    }

    override suspend fun sendNotification(method: String, params: Any?, timeout: Duration) {
        val notification = JsonRpcNotification(
            method = method,
            params = params?.let { jsonMapper.valueToTree(it) }
        )

        writeLine(notification, timeout)
    }

    override suspend fun <T> sendRequest(method: String, params: Any?, responseType: TypeReference<T>, timeout: Duration): T {
        val request = JsonRpcRequest(
            id = generateId(),
            method = method,
            params = params?.let { jsonMapper.valueToTree(it) }
        )

        val deferred = CompletableDeferred<JsonRpcResponse>()
        pendingRequests[request.id] = deferred

        try {
            val response = withTimeout(timeout) {
                writeLine(request)
                deferred.await()
            }

            return when (response) {
                is JsonRpcSuccess -> jsonMapper.treeToValue(response.result, responseType)
                is JsonRpcFailure -> throw RequestFailureException(response.error.code, response.error.message, response.error.data)
            }
        } finally {
            pendingRequests.remove(request.id)
        }
    }

    override suspend fun respondWithSuccess(id: String, response: Any, timeout: Duration) {
        val success = JsonRpcSuccess(
            id = id,
            result = jsonMapper.valueToTree(response)
        )

        writeLine(success, timeout)
    }

    override suspend fun respondWithFailure(id: String, code: Int, message: String, data: Any?, timeout: Duration) {
        val failure = JsonRpcFailure(
            id = id,
            error = JsonRpcError(
                code = code,
                message = message,
                data = data?.let { jsonMapper.valueToTree(it) }
            )
        )

        writeLine(failure, timeout)
    }

    // Reading
    private var readerJob: Job? = null

    private suspend fun readIncomingMessages() {
        val buffer = StringBuilder()

        port.incomingBytes.collect { bytes ->
            buffer.append(String(bytes))

            while (true) {
                val newlineIndex = buffer.indexOf('\n')

                if (newlineIndex == -1) break

                val line = buffer.substring(0, newlineIndex).trim()
                buffer.delete(0, newlineIndex + 1)

                if (line.isNotEmpty()) routeMessage(line)
            }
        }
    }

    private fun routeMessage(json: String) {
        val message = try {
            parseMessage(json)
        } catch (e: Exception) {
            Logger.warning("Port $name received malformed JSON: '$json' - ${e.message}")
            return
        }

        when (message) {
            is JsonRpcSuccess -> pendingRequests[message.id]?.complete(message)
            is JsonRpcFailure -> pendingRequests[message.id]?.complete(message)
            is JsonRpcRequest -> _incomingRequests.tryEmit(message)
            is JsonRpcNotification -> _incomingNotifications.tryEmit(message)
        }
    }

    private fun parseMessage(json: String): JsonRpcMessage {
        val node = jsonMapper.readTree(json)

        val hasId = node.has("id")
        val hasMethod = node.has("method")

        return when {
            hasMethod && hasId -> jsonMapper.treeToValue<JsonRpcRequest>(node)
            hasMethod -> jsonMapper.treeToValue<JsonRpcNotification>(node)
            hasId && node.has("result") -> jsonMapper.treeToValue<JsonRpcSuccess>(node)
            hasId && node.has("error") -> jsonMapper.treeToValue<JsonRpcFailure>(node)
            else -> throw IllegalArgumentException("Unrecognized JSON-RPC message")
        }
    }

    // Helpers
    private suspend fun writeLine(message: JsonRpcMessage, timeout: Duration) {
        withTimeout(timeout) { writeLine(message) }
    }

    private suspend fun writeLine(message: JsonRpcMessage) {
        val json = jsonMapper.writeValueAsBytes(message) + '\n'.code.toByte()

        port.write(json)
    }

}

@SkipCoverage
class SerialJsonRpcConnectionFactory {
    fun create(port: SerialPort): SerialJsonRpcConnection = RealSerialJsonRpcConnection(port)
}