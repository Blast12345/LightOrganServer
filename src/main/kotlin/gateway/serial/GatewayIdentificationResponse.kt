package gateway.serial

class GatewayIdentificationResponse(
    val messageSender: String,
    val messageRecipient: String
) : SerialMessage {
    override val messageType: String = "gateway-identification-response"
}