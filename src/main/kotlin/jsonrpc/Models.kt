package jsonrpc

import com.fasterxml.jackson.annotation.JsonInclude
import tools.jackson.databind.JsonNode

sealed interface JsonRpcMessage {
    val jsonrpc: String get() = "2.0"
}

// Calls
sealed interface JsonRpcCall : JsonRpcMessage {
    val method: String
    val params: JsonNode?
}

data class JsonRpcRequest(
    val id: String,
    override val method: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    override val params: JsonNode?,
) : JsonRpcCall

data class JsonRpcNotification(
    override val method: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    override val params: JsonNode?,
) : JsonRpcCall


// Responses
sealed interface JsonRpcResponse : JsonRpcMessage {
    val id: String
}

data class JsonRpcSuccess(
    override val id: String,
    val result: JsonNode
) : JsonRpcResponse

data class JsonRpcFailure(
    override val id: String,
    val error: JsonRpcError
) : JsonRpcResponse

// Errors
data class JsonRpcError(
    val code: Int,
    val message: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val data: JsonNode?
)

object JsonRpcErrorCodes {
    const val PARSE_ERROR = -32700
    const val INVALID_REQUEST = -32600
    const val METHOD_NOT_FOUND = -32601
    const val INVALID_PARAMS = -32602
    const val INTERNAL_ERROR = -32603
}