package toolkit.monkeyTest

import gateway.messages.GatewayIdentificationRequest

fun nextGatewayIdentificationRequest(): GatewayIdentificationRequest {
    return GatewayIdentificationRequest(
        requestId = nextString("requestId"),
    )
}