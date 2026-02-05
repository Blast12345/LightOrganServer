package gateway.serial

data class FakeSerialRequest(
    override val requestId: String,
    override val type: String
) : SerialRequest