package lightOrgan.gateway

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.gateway.GatewayManager.Event
import lightOrgan.gateway.GatewayManager.State

class FakeGatewayManager : GatewayManager {

    override val state = MutableStateFlow<State>(State.NoGateway)
    override val events = MutableSharedFlow<Event>()

    // Connect
    val gateway = FakeGateway()
    var connectError: Throwable? = null

    override suspend fun connect() {
        connectError?.let { throw it }
        state.value = State.Connected(gateway)
    }

    // Disconnect
    var disconnectError: Throwable? = null

    override suspend fun disconnect() {
        disconnectError?.let { throw it }
        state.value = State.NoGateway
    }

}