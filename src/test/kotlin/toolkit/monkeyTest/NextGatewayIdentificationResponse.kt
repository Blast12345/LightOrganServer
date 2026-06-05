package toolkit.monkeyTest

import lightOrgan.gateway.GatewayIdentificationResponse

fun nextGatewayIdentificationResponse(): GatewayIdentificationResponse {
    return GatewayIdentificationResponse(
        macAddress = nextString("macAddress"),
        firmwareVersion = nextString("firmwareVersion")
    )
}