package lightOrgan.gateway.serial.messages

data class GatewayIdentificationResponse(
    val macAddress: String,
    val firmwareVersion: String
)