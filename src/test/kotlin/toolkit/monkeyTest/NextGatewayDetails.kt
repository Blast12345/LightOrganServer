package toolkit.monkeyTest

import lightOrgan.gateway.GatewayDetails
import lightOrgan.gateway.SerialGatewayDetails

fun nextGatewayDetails(): GatewayDetails {
    return SerialGatewayDetails(
        macAddress = nextString("mac"),
        firmwareVersion = nextString("version"),
        baudRate = nextInt(),
        frameFormat = nextSerialFrameFormat()
    )
}