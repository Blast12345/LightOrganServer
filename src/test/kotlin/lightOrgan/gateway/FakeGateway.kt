package lightOrgan.gateway

import color.StandardRgbColor
import kotlinx.coroutines.flow.MutableStateFlow
import toolkit.monkeyTest.nextGatewayDetails

class FakeGateway : Gateway {

    override val details: Gateway.Details = nextGatewayDetails()
    override val isConnected = MutableStateFlow(true)

    override suspend fun disconnect() {
        isConnected.value = false
    }

    var lastColor: StandardRgbColor? = null

    override suspend fun broadcastColor(color: StandardRgbColor) {
        lastColor = color
    }

}