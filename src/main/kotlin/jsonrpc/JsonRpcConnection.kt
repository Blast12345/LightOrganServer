package jsonrpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import tools.jackson.core.type.TypeReference
import tools.jackson.module.kotlin.jacksonTypeRef
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

// REFERENCE: https://www.jsonrpc.org/specification
// I could not cleanly represent IDs as both numbers and strings, so I picked strings.
interface JsonRpcConnection {
    val isConnected: StateFlow<Boolean>
    suspend fun connect()
    suspend fun disconnect()

    // Protocol
    val incomingRequests: Flow<JsonRpcRequest>
    val incomingNotifications: Flow<JsonRpcNotification>

    suspend fun <T> sendRequest(method: String, params: Any?, responseType: TypeReference<T>, timeout: Duration = 5.seconds): T
    suspend fun sendNotification(method: String, params: Any?, timeout: Duration = 5.seconds)
    suspend fun respondWithSuccess(id: String, response: Any, timeout: Duration = 5.seconds)
    suspend fun respondWithFailure(id: String, code: Int, message: String, data: Any?, timeout: Duration = 5.seconds)
}

suspend inline fun <reified T> JsonRpcConnection.sendRequest(
    method: String,
    params: Any? = null,
    timeout: Duration = 5.seconds,
): T = sendRequest(method, params, jacksonTypeRef<T>(), timeout)