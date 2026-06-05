package toolkit.monkeyTest

import lightOrgan.gateway.Gateway
import lightOrgan.gateway.SerialGatewayDetails

fun nextGatewayDetails(): Gateway.Details {
    return SerialGatewayDetails(
        macAddress = nextString("mac"),
        firmwareVersion = nextString("version"),
        baudRate = nextInt(),
        frameFormat = nextSerialFrameFormat()
    )
}