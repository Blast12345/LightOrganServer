package gui.tiles.gateway

import gui.dashboard.SnackbarController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lightOrgan.gateway.GatewayManager

class GatewayTileViewModel(
    private val gatewayManager: GatewayManager,
    private val snackbarController: SnackbarController,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    val isSearching = gatewayManager.isSearching
    val gatewayDetails = gatewayManager.gatewayDetails
    val isConnected = gatewayManager.isConnected

    fun findGateway() {
        scope.launch {
            try {
                gatewayManager.findGateway()
            } catch (e: Exception) {
                snackbarController.show(e.message ?: "Failed to find gateway.")
            }
        }
    }

    fun connect() {
//        scope.launch {
//            try {
//                gatewayManager.connect()
//            } catch (e: Exception) {
//                snackbarController.show(e.message ?: "Failed to connect to gateway.")
//            }
//        }
    }

    fun disconnect() {
//        scope.launch {
//            try {
//                gatewayManager.disconnect()
//            } catch (e: Exception) {
//                snackbarController.show(e.message ?: "Failed to disconnect from gateway.")
//            }
//        }
    }

}