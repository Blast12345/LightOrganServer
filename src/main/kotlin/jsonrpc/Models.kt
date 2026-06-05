package jsonrpc

import com.fasterxml.jackson.annotation.JsonInclude
import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.ContainerNode

sealed interface JsonRpcMessage {
    val jsonrpc: String
}

// Calls
sealed interface JsonRpcCall : JsonRpcMessage

data class JsonRpcRequest(
    val id: String,
    val method: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL) val params: ContainerNode<*>?,
    override val jsonrpc: String = "2.0"
) : JsonRpcCall

data class JsonRpcNotification(
    val method: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL) val params: ContainerNode<*>?,
    override val jsonrpc: String = "2.0"
) : JsonRpcCall

// Responses
sealed interface JsonRpcResponse : JsonRpcMessage

data class JsonRpcSuccess(
    val id: String,
    val result: JsonNode,
    override val jsonrpc: String = "2.0"
) : JsonRpcResponse

data class JsonRpcFailure(
    val id: String?,
    val error: JsonRpcError,
    override val jsonrpc: String = "2.0"
) : JsonRpcResponse

// Errors
data class JsonRpcError(
    val code: Int,
    val message: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL) val data: JsonNode?
)

object JsonRpcErrorCodes {
    const val PARSE_ERROR = -32700
    const val INVALID_REQUEST = -32600
    const val METHOD_NOT_FOUND = -32601
    const val INVALID_PARAMS = -32602
    const val INTERNAL_ERROR = -32603
}