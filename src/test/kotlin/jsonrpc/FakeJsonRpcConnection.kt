package jsonrpc

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import tools.jackson.core.type.TypeReference
import kotlin.time.Duration

class FakeJsonRpcConnection : JsonRpcConnection {

    override val isConnected = MutableStateFlow<Boolean>(true)
    override val incomingRequests = MutableSharedFlow<JsonRpcRequest>()
    override val incomingNotifications = MutableSharedFlow<JsonRpcNotification>()

    override suspend fun connect() {
        isConnected.value = true
    }

    override suspend fun disconnect() {
        isConnected.value = false
    }

    // Requests
    override suspend fun <T> sendRequest(
        method: String,
        params: Any?,
        responseType: TypeReference<T>,
        timeout: Duration
    ): T {
        TODO("Not yet implemented")
    }

    // Notifications
    data class RecordedNotification(val method: String, val params: Any?)

    val notifications = mutableListOf<RecordedNotification>()

    override suspend fun sendNotification(method: String, params: Any?, timeout: Duration) {
        notifications.add(RecordedNotification(method, params))
    }

    override suspend fun respondWithSuccess(id: String, response: Any, timeout: Duration) {
        TODO("Not yet implemented")
    }

    override suspend fun respondWithFailure(id: String, code: Int, message: String, data: Any?, timeout: Duration) {
        TODO("Not yet implemented")
    }

}