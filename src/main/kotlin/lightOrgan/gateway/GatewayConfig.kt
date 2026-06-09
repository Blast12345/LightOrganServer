package lightOrgan.gateway

import serial.SerialFrameFormat

data class GatewayConfig(
    val baudRate: Int,
    val frameFormat: SerialFrameFormat
)

// TODO: Move me
data class GatewayIdentificationResponse(
    val macAddress: String,
    val firmwareVersion: String
)