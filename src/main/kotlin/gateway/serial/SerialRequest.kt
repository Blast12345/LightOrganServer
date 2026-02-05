package gateway.serial

interface SerialRequest : SerialObject {
    val requestId: String
    override val type: String
}