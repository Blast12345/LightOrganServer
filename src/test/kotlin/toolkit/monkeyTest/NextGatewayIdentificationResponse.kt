package toolkit.monkeyTest

import lightOrgan.gateway.serial.messages.GatewayIdentificationResponse

fun nextGatewayIdentificationResponse(): GatewayIdentificationResponse {
    return GatewayIdentificationResponse(
        macAddress = nextString("macAddress"),
        firmwareVersion = nextString("firmwareVersion")
    )
}