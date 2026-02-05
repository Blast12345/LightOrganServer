package gateway.messages

import gateway.serial.SerialRequest

data class GatewayIdentificationRequest(
    override val requestId: String,
) : SerialRequest {

    override val type: String = "gateway-identification"

}