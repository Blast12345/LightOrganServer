package gui.tiles.gateway

import extensions.tryLaunch
import gui.dashboard.snackbar.SnackbarController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lightOrgan.gateway.GatewayManager

class GatewayTileViewModel(
    private val gatewayManager: GatewayManager,
    private val snackbarController: SnackbarController,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    val connectionState = gatewayManager.connectionState

    init {
        gatewayManager.events
            .onEach { snackbarController.show(it.toUserMessage()) }
            .launchIn(scope)
    }

    private fun GatewayManager.Event.toUserMessage(): String = when (this) {
        GatewayManager.Event.UnexpectedDisconnect -> "Gateway connection lost."
    }

    fun connect() {
        scope.tryLaunch(
            block = { gatewayManager.connect() },
            onError = { snackbarController.show(it.message ?: "Failed to connect to the gateway.") }
        )
    }

    fun disconnect() {
        scope.tryLaunch(
            block = { gatewayManager.disconnect() },
            onError = { snackbarController.show(it.message ?: "Failed to disconnect from the gateway.") }
        )
    }

}