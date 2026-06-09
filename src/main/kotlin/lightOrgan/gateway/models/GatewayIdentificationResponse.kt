package lightOrgan.gateway.models

data class GatewayIdentificationResponse(
    val macAddress: String,
    val firmwareVersion: String
)