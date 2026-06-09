package lightOrgan.gateway

import serial.SerialFrameFormat

sealed interface GatewayDetails {
    val macAddress: String
    val firmwareVersion: String
}

data class SerialGatewayDetails(
    override val macAddress: String,
    override val firmwareVersion: String,
    val baudRate: Int,
    val frameFormat: SerialFrameFormat,
) : GatewayDetails