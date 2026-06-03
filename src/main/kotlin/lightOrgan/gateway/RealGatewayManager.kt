package lightOrgan.gateway

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

interface GatewayManager {
    val connectionState: StateFlow<GatewayManagerState>
    val events: SharedFlow<GatewayEvent>

    suspend fun connect()
    suspend fun disconnect()
}

sealed interface GatewayManagerState {
    data object NoGateway : GatewayManagerState
    data object Connecting : GatewayManagerState
    data class Connected(val gateway: Gateway) : GatewayManagerState // TODO: Should expose disconnect?
}

sealed interface GatewayEvent {
    data object ConnectionLost : GatewayEvent
}


// ENHANCEMENT: Auto-find
// ENHANCEMENT: Auto-reconnect
@OptIn(ExperimentalCoroutinesApi::class)
class RealGatewayManager(
    private val gatewayFinder: GatewayFinder = GatewayFinder(),
//    private val scope: CoroutineScope,
) : GatewayManager {

    private val _connectionState = MutableStateFlow<GatewayManagerState>(GatewayManagerState.NoGateway)
    override val connectionState: StateFlow<GatewayManagerState> = _connectionState.asStateFlow()

    private val _events = MutableSharedFlow<GatewayEvent>()
    override val events: SharedFlow<GatewayEvent> = _events.asSharedFlow()

//    private var unexpectedDisconnectWatcher: Job? = null

    override suspend fun connect() {
        check(_connectionState.value is GatewayManagerState.NoGateway) { "Cannot connect at this time. Invalid connection state." }

        _connectionState.value = GatewayManagerState.Connecting

        try {
            val gateway = gatewayFinder.find() ?: throw GatewayNotFound()
            // TODO: Unexpected DC
            _connectionState.value = GatewayManagerState.Connected(gateway)
        } catch (e: Exception) {
            _connectionState.value = GatewayManagerState.NoGateway
            throw e
        }
    }

    override suspend fun disconnect() {
        val current = _connectionState.value
        check(current is GatewayManagerState.Connected) { "Cannot disconnect at this time. No gateway connected." }

//        unexpectedDisconnectWatcher?.cancel()
        current.gateway.disconnect()
        _connectionState.value = GatewayManagerState.NoGateway
    }

    // Types
    class GatewayNotFound : Exception()

}