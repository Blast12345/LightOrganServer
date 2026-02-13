package gateway.serial

import JsonMapper
import gateway.serial.wrappers.SerialFormat
import gateway.serial.wrappers.SerialPort
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import logging.Logger
import tools.jackson.databind.JsonNode
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SerialRouter(
    private val serialPort: SerialPort,
    private val baudRate: Int,
    private val format: SerialFormat,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    companion object {

        fun create(
            port: SerialPort,
            baudRate: Int,
            serialFormat: SerialFormat
        ): SerialRouter {
            return SerialRouter(port, baudRate, serialFormat)
        }

    }

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(serialPort.isOpen)
    val isConnected = _isConnected.asStateFlow()
    private val pendingRequests = ConcurrentHashMap<String, CompletableDeferred<JsonNode>>()

    init {
        updateConnectionState()
    }

    private fun updateConnectionState() {
        val stateChanged = _isConnected.value != serialPort.isOpen

        if (stateChanged) {
            _isConnected.value = serialPort.isOpen
        }
    }

    fun connect() {
        try {
            serialPort.open(baudRate, format)
            scope.launch { readLoop() }
        } finally {
            updateConnectionState()
        }
    }

    private suspend fun readLoop() {
        try {
            while (currentCoroutineContext().isActive) {
                // Check port status before reading
                if (!serialPort.isOpen) {
                    break
                }

                val line = serialPort.readNextLine() ?: run {
                    delay(1)
                    continue
                }

                val json = parse(line) ?: continue

                val requestId = json.get("request-id")?.asString()

                if (requestId != null) {
                    pendingRequests[requestId]?.complete(json)
                } else {
                    // TODO: Received object
                }
            }
        } finally {
            updateConnectionState()
        }
    }

    private fun parse(line: String): JsonNode? {
        return runCatching {
            JsonMapper.readTree(line)
        }.onFailure { e ->
            Logger.error("Failed to parse JSON: $line - ${e.message}")
        }.getOrNull()
    }

    fun disconnect() {
        try {
            scope.cancel()
            serialPort.close()
        } finally {
            updateConnectionState()
        }
    }

    suspend fun <Request : SerialRequest, Response : SerialResponse> send(
        request: Request,
        responseClass: Class<Response>,
        timeout: Duration = 100.milliseconds
    ): Response {
        val deferred = CompletableDeferred<JsonNode>()
        pendingRequests[request.requestId] = deferred

        try {
            serialPort.write(request)
            val json = withTimeout(timeout) { deferred.await() }
            return JsonMapper.treeToValue(json, responseClass)
        } catch (e: Exception) {
            updateConnectionState()
            throw e
        } finally {
            pendingRequests.remove(request.requestId)
        }
    }

    fun send(obj: SerialObject) {
        try {
            serialPort.write(obj)
        } catch (e: Exception) {
            updateConnectionState()
            throw e
        }
    }

    private fun SerialPort.write(obj: SerialObject) {
        val string = JsonMapper.writeValueAsString(obj)
        writeLine(string)
    }

}