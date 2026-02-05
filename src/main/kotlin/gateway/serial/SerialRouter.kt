package gateway.serial

import JsonMapper
import gateway.serial.wrappers.SerialFormat
import gateway.serial.wrappers.SerialPort
import kotlinx.coroutines.*
import logging.Logger
import tools.jackson.databind.JsonNode
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

enum class ConnectionState {
    CONNECTED,
    DISCONNECTED
}

class SerialRouter(
    private val serialPort: SerialPort,
    private val baudRate: Int,
    private val format: SerialFormat,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

//    val connectionState: StateFlow<ConnectionState>

    companion object {

        fun create(
            port: SerialPort,
            baudRate: Int,
            serialFormat: SerialFormat
        ): SerialRouter {
            return SerialRouter(port, baudRate, serialFormat)
        }

    }

    private val pendingRequests = ConcurrentHashMap<String, CompletableDeferred<JsonNode>>()

    // TODO: Should a prevent duplicate "connect" calls?
    fun connect() {
        serialPort.open(baudRate, format)
        scope.launch { readLoop() }
    }

    private suspend fun readLoop() {
        while (currentCoroutineContext().isActive) {
            val line = serialPort.readNextLine() ?: run { delay(1); continue }
            val json = parse(line) ?: continue

            val requestId = json.get("request-id")?.asString()

            if (requestId != null) {
                // TODO: Determine if the json is a request or response
                pendingRequests[requestId]?.complete(json)
            } else {
                // TODO: Received object
            }
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
        scope.cancel()
        serialPort.close()
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
        } finally {
            pendingRequests.remove(request.requestId)
        }
    }

    fun send(obj: SerialObject) {
        serialPort.write(obj)
    }

    private fun SerialPort.write(obj: SerialObject) {
        val string = JsonMapper.writeValueAsString(obj)

        writeLine(string)
    }

}