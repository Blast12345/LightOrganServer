package gateway.messages

import gateway.serial.SerialResponse

data class GatewayIdentificationResponse(
    override val requestId: String,
    override val type: String = "gateway-identification",
    val macAddress: String,
    val firmwareVersion: String
) : SerialResponse