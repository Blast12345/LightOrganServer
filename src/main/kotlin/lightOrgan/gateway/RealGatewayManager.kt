package lightOrgan.gateway

import color.StandardRgbColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

interface GatewayManager {
    val connectionState: StateFlow<GatewayManagerState>
    val events: SharedFlow<GatewayEvent>

    suspend fun connect()
    suspend fun disconnect()

    suspend fun broadcastColor(color: StandardRgbColor)
}

sealed interface GatewayManagerState {
    data object NoGateway : GatewayManagerState
    data object Connecting : GatewayManagerState
    data class Connected(val details: GatewayDetails) : GatewayManagerState
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

    private var gateway: Gateway? = null

    private val _connectionState = MutableStateFlow<GatewayManagerState>(GatewayManagerState.NoGateway)
    override val connectionState: StateFlow<GatewayManagerState> = _connectionState.asStateFlow()

    private val _events = MutableSharedFlow<GatewayEvent>()
    override val events: SharedFlow<GatewayEvent> = _events.asSharedFlow()

//    private var unexpectedDisconnectWatcher: Job? = null

    // Lifecycle
    override suspend fun connect() {
        check(_connectionState.value is GatewayManagerState.NoGateway) { "Cannot connect at this time. Invalid connection state." }

        _connectionState.value = GatewayManagerState.Connecting

        try {
            gateway = gatewayFinder.find() ?: throw GatewayNotFound()
            // TODO: Unexpected DC
            _connectionState.value = GatewayManagerState.Connected(gateway!!.details)
        } catch (e: Exception) {
            _connectionState.value = GatewayManagerState.NoGateway
            throw e
        }
    }

    override suspend fun disconnect() {
        check(gateway != null) { "Cannot disconnect at this time. No gateway connected." }

//        unexpectedDisconnectWatcher?.cancel()
        gateway!!.disconnect()
        _connectionState.value = GatewayManagerState.NoGateway
    }

    class GatewayNotFound : Exception()

    // Operations
    override suspend fun broadcastColor(color: StandardRgbColor) {
        check(gateway != null) { "Cannot broadcast color. No gateway connected." }

        gateway!!.broadcastColor(color)
    }

}