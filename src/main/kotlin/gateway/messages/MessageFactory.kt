package gateway.messages

import wrappers.color.Color
import java.util.*

// TODO: Test
class MessageFactory {

    fun createIdentificationRequest(): GatewayIdentificationRequest {
        return GatewayIdentificationRequest(
            requestId = UUID.randomUUID().toString()
        )
    }

    fun createColorCommand(color: Color): GatewaySetColorCommand {
        return GatewaySetColorCommand(
            red = color.red,
            green = color.green,
            blue = color.blue
        )
    }

}