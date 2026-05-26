package lightOrgan.gateway

import wrappers.serial.SerialFormat

data class GatewayConfig(
    val baudRate: Int,
    val serialFormat: SerialFormat
)