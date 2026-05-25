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

    val gatewayDetails: Flow<GatewayDetails?> = currentGateway.map { it?.details }
    val isConnected: Flow<Boolean> = currentGateway.map { it?.isConnected == true }
    val isSearching: Flow<Boolean> = gatewayFinder.isSearching

    suspend fun findGateway() {
        if (currentGateway.value != null) return
        currentGateway.value = gatewayFinder.find()
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