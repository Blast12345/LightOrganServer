package gateway.serial

interface SerialResponse : SerialObject {
    val requestId: String
    override val type: String
}