package gateway.serial

interface SerialRequest<T : SerialResponse> : SerialObject {
    val requestId: String
    override val type: String
}