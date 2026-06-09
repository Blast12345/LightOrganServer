package lightOrgan.gateway

import serial.SerialFrameFormat

data class GatewayConfig(
    val baudRate: Int,
    val frameFormat: SerialFrameFormat
)