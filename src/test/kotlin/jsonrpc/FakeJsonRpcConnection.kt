package jsonrpc

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import tools.jackson.core.type.TypeReference
import kotlin.time.Duration

open class FakeJsonRpcConnection : JsonRpcConnection {

    override val isConnected = MutableStateFlow<Boolean>(true)
    override val incomingRequests = MutableSharedFlow<JsonRpcRequest>()
    override val incomingNotifications = MutableSharedFlow<JsonRpcNotification>()

    // Lifecycle
    var connectError: Exception? = null

    override suspend fun connect() {
        connectError?.let { throw it }
        isConnected.value = true
    }

    override suspend fun disconnect() {
        isConnected.value = false
    }

    // Requests
    private data class RequestKey(val method: String, val params: Any?, val timeout: Duration)

    private val responses = mutableMapOf<RequestKey, Any?>()
    var sendRequestError: Exception? = null

    override suspend fun <T> sendRequest(
        method: String,
        params: Any?,
        responseType: TypeReference<T>,
        timeout: Duration
    ): T {
        check(isConnected.value) { "Not connected" }
        sendRequestError?.let { throw it }

        val key = RequestKey(method, params, timeout)
        check(key in responses) { "No stubbed response for '$method' with params '$params' and timeout '$timeout'" }

        @Suppress("UNCHECKED_CAST")
        return responses[key] as T
    }

    fun <T> stubResponse(method: String, params: Any? = null, timeout: Duration, response: T) {
        responses[RequestKey(method, params, timeout)] = response
    }

    // Notifications
    data class RecordedNotification(val method: String, val params: Any?, val timeout: Duration)

    val notifications = mutableListOf<RecordedNotification>()

    override suspend fun sendNotification(method: String, params: Any?, timeout: Duration) {
        check(isConnected.value) { "Not connected" }
        notifications.add(RecordedNotification(method, params, timeout))
    }

    // Responses
    override suspend fun respondWithSuccess(id: String, response: Any, timeout: Duration) {
        TODO("Not yet implemented")
    }

    override suspend fun respondWithFailure(id: String, code: Int, message: String, data: Any?, timeout: Duration) {
        TODO("Not yet implemented")
    }

}