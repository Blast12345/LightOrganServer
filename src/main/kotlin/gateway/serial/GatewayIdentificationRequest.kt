package gateway.serial

class GatewayIdentificationRequest : SerialMessage {
    override val messageType: String = "gateway-identification-request"
}