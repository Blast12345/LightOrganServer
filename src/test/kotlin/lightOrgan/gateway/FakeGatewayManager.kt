package lightOrgan.gateway

import color.StandardRgbColor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import toolkit.monkeyTest.nextGatewayDetails

class FakeGatewayManager : GatewayManager {

    override val connectionState = MutableStateFlow<GatewayManagerState>(GatewayManagerState.NoGateway)
    override val events = MutableSharedFlow<GatewayEvent>()

    // Connect
    val gatewayDetails = nextGatewayDetails()
    var connectError: Throwable? = null

    override suspend fun connect() {
        connectError?.let { throw it }
        connectionState.value = GatewayManagerState.Connected(gatewayDetails)
    }

    // Disconnect
    var disconnectError: Throwable? = null

    override suspend fun disconnect() {
        disconnectError?.let { throw it }
        connectionState.value = GatewayManagerState.NoGateway
    }

    // Broadcast color
    var lastColor: StandardRgbColor? = null

    override suspend fun broadcastColor(color: StandardRgbColor) {
        lastColor = color
    }
}