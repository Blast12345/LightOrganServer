package lightOrgan.gateway

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lightOrgan.gateway.GatewayManager.Event
import lightOrgan.gateway.GatewayManager.State

// ENHANCEMENT: Auto-find
// ENHANCEMENT: Auto-reconnect
@OptIn(ExperimentalCoroutinesApi::class)
class RealGatewayManager(
    private val gatewayFinder: GatewayFinder = RealGatewayFinder(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
) : GatewayManager {

    private val _connectionState = MutableStateFlow<State>(State.NoGateway)
    override val state: StateFlow<State> = _connectionState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    override val events: SharedFlow<Event> = _events.asSharedFlow()

    private var unexpectedDisconnectWatcher: Job? = null

    // Lifecycle
    override suspend fun connect() {
        when (_connectionState.value) {
            is State.NoGateway -> Unit
            is State.Connecting -> throw IllegalStateException("Could not connect: there is a connection attempt in progress.")
            is State.Connected -> throw IllegalStateException("Could not connect: there is already a gateway connected.")
        }

        _connectionState.value = State.Connecting

        try {
            val gateway = gatewayFinder.find()
            check(gateway.isConnected.value) { "Could not connect: the gateway is already disconnected." }
            _connectionState.value = State.Connected(gateway)
            unexpectedDisconnectWatcher = watchForUnexpectedDisconnect(gateway)
        } catch (e: Exception) {
            _connectionState.value = State.NoGateway
            throw e
        }
    }

    private fun watchForUnexpectedDisconnect(gateway: Gateway): Job {
        return scope.launch {
            gateway.isConnected.first { !it }
            _connectionState.value = State.NoGateway
            _events.emit(Event.UnexpectedDisconnect)
        }
    }

    override suspend fun disconnect() {
        when (val state = _connectionState.value) {
            is State.NoGateway -> {
                throw IllegalStateException("Could not disconnect: there is no gateway connected.")
            }

            is State.Connecting -> {
                throw IllegalStateException("Could not disconnect: there is a connection attempt in progress.")
            }

            is State.Connected -> {
                unexpectedDisconnectWatcher?.cancel()
                state.gateway.disconnect()
                _connectionState.value = State.NoGateway
            }
        }
    }


}