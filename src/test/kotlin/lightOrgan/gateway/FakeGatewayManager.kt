package lightOrgan.gateway

import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGatewayManager : GatewayManager {

    val gateway: Gateway = mockk() // TODO: Replace me with a fake gateway

    override val connectionState = MutableStateFlow<GatewayManagerState>(GatewayManagerState.NoGateway)
    override val events = MutableSharedFlow<GatewayEvent>()

    // Connect
    var connectError: Throwable? = null

    override suspend fun connect() {
        connectError?.let { throw it }
        connectionState.value = GatewayManagerState.Connected(gateway)
    }

    // Disconnect
    var disconnectError: Throwable? = null

    override suspend fun disconnect() {
        disconnectError?.let { throw it }
        connectionState.value = GatewayManagerState.NoGateway
    }
}