package toolkit.monkeyTest

import gateway.messages.GatewayIdentificationResponse

fun nextGatewayIdentificationResponse(): GatewayIdentificationResponse {
    return GatewayIdentificationResponse(
        requestId = nextString("requestId"),
        macAddress = nextString("macAddress"),
        firmwareVersion = nextString("firmwareVersion")
    )
}