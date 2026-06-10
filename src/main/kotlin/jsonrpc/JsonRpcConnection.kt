package jsonrpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import tools.jackson.core.type.TypeReference
import tools.jackson.module.kotlin.jacksonTypeRef
import kotlin.time.Duration

interface JsonRpcConnection : JsonRpcProtocol {
    val isConnected: StateFlow<Boolean>
    suspend fun connect()
    suspend fun disconnect()
}

// REFERENCE: https://www.jsonrpc.org/specification
interface JsonRpcProtocol {
    val incomingNotifications: Flow<JsonRpcNotification>
    val incomingRequests: Flow<JsonRpcRequest>

    suspend fun sendNotification(method: String, params: Any?, timeout: Duration)
    suspend fun <T> sendRequest(method: String, params: Any?, responseType: TypeReference<T>, timeout: Duration): T
    suspend fun respondWithSuccess(id: String, response: Any, timeout: Duration)
    suspend fun respondWithFailure(id: String, code: Int, message: String, data: Any?, timeout: Duration)
}

suspend inline fun <reified T> JsonRpcProtocol.sendRequest(
    method: String,
    params: Any? = null,
    timeout: Duration,
): T = sendRequest(method, params, jacksonTypeRef<T>(), timeout)

// Exceptions
class RequestFailureException(
    val code: Int,
    override val message: String,
    val data: Any?
) : Exception(message)