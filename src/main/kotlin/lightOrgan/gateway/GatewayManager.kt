package lightOrgan.gateway

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface GatewayManager {
    val connectionState: StateFlow<State>
    val events: SharedFlow<Event>

    suspend fun connect()
    suspend fun disconnect()

    // Types
    sealed interface State {
        data object NoGateway : State
        data object Connecting : State
        data class Connected(val gateway: Gateway) : State
    }

    sealed interface Event {
        data object UnexpectedDisconnect : Event
    }
}