package toolkit.monkeyTest

import lightOrgan.gateway.models.GatewayIdentificationResponse

fun nextGatewayIdentificationResponse(): GatewayIdentificationResponse {
    return GatewayIdentificationResponse(
        macAddress = nextString("macAddress"),
        firmwareVersion = nextString("firmwareVersion")
    )
}