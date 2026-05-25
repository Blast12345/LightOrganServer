package lightOrgan.gateway

import color.StandardRgbColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// ENHANCEMENT: Auto-reconnect
class GatewayManager(
    private val currentGateway: MutableStateFlow<Gateway?> = MutableStateFlow(null),
    private val gatewayFinder: GatewayFinder = GatewayFinder()
) {

    private val _isSearching = MutableStateFlow(false)

    val gatewayDetails: Flow<GatewayDetails?> = currentGateway.map { it?.details }
    val isConnected: Flow<Boolean> = currentGateway.map { it?.isConnected == true }
    val isSearching: Flow<Boolean> = _isSearching

    suspend fun findGateway() {
        if (_isSearching.value) return
        if (currentGateway.value != null) return

        try {
            _isSearching.value = true
            currentGateway.value = gatewayFinder.find()
        } finally {
            _isSearching.value = false
        }
    }

    fun send(color: StandardRgbColor) {
        TODO()
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

}