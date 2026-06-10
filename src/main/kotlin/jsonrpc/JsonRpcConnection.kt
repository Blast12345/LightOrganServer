package jsonrpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import tools.jackson.core.type.TypeReference
import tools.jackson.module.kotlin.jacksonTypeRef
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface JsonRpcConnection : JsonRpcProtocol {
    val isConnected: StateFlow<Boolean>
    suspend fun connect()
    suspend fun disconnect()
}

// REFERENCE: https://www.jsonrpc.org/specification
interface JsonRpcProtocol {
    val incomingNotifications: Flow<JsonRpcNotification>
    val incomingRequests: Flow<JsonRpcRequest>

    suspend fun sendNotification(method: String, params: Any?, timeout: Duration = 5.seconds)
    suspend fun <T> sendRequest(method: String, params: Any?, responseType: TypeReference<T>, timeout: Duration = 5.seconds): T
    suspend fun respondWithSuccess(id: String, response: Any, timeout: Duration = 5.seconds)
    suspend fun respondWithFailure(id: String, code: Int, message: String, data: Any?, timeout: Duration = 5.seconds)
}

// Exceptions
class RequestFailureException(
    val code: Int,
    override val message: String,
    val data: Any?
) : Exception(message)

// Convenience functions
suspend inline fun <reified T> JsonRpcConnection.sendRequest(
    method: String,
    params: Any? = null,
    timeout: Duration = 5.seconds,
): T = sendRequest(method, params, jacksonTypeRef<T>(), timeout)