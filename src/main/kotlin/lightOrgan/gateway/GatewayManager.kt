package lightOrgan.gateway

import color.StandardRgbColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*


// ENHANCEMENT: Auto-find
// ENHANCEMENT: Auto-reconnect
@OptIn(ExperimentalCoroutinesApi::class)
class GatewayManager(
    private val gatewayFinder: GatewayFinder = GatewayFinder()
) {

    private val _currentGateway: MutableStateFlow<Gateway?> = MutableStateFlow(null)
    private val _isSearching = MutableStateFlow(false)

    val gatewayDetails: Flow<GatewayDetails?> = _currentGateway.map { it?.details }
    val isConnected: Flow<Boolean> = _currentGateway.flatMapLatest { it?.isConnected ?: flowOf(false) }
    val isSearching: Flow<Boolean> = _isSearching.asStateFlow()

    suspend fun findGateway() {
        if (_currentGateway.value != null) return
        if (_isSearching.value) return

        try {
            _isSearching.value = true
            _currentGateway.value = gatewayFinder.find()
        } finally {
            _isSearching.value = false
        }
    }

    // TODO: Separate find and connect?
    suspend fun connect() {
//        if (currentGateway.value != null) {
//            currentGateway.value?.reconnect() // TODO: Update test
//            return
//        }
//
//        if (isSearching.value) {
//            throw IllegalStateException("Already attempting to connect. Please wait.")
//        }
    }

    fun disconnect() {
//        _currentGateway.value?.disconnect()
//        _currentGateway.value = null
    }

    fun send(color: StandardRgbColor) {
        TODO()
    }


}