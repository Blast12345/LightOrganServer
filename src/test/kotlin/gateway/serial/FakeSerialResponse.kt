package gateway.serial

data class FakeSerialResponse(
    override val requestId: String,
    override val type: String
) : SerialResponse