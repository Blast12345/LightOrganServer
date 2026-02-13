package gui.dashboard.tiles.gateway

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import gateway.Gateway
import gateway.GatewayManager
import gui.SnackbarErrorHandler
import gui.UIErrorHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GatewayTileViewModel private constructor(
    private val gatewayManager: GatewayManager,
    private val errorHandler: UIErrorHandler,
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) {

    companion object {
        fun create(
            gatewayManager: GatewayManager,
            errorHandler: UIErrorHandler = SnackbarErrorHandler,
            scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
            ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        ): GatewayTileViewModel {
            return GatewayTileViewModel(
                gatewayManager,
                errorHandler,
                scope,
                ioDispatcher
            )
        }
    }

    // TODO: Reconnect state? We have gateway, but it's not connected.
    var isSearching by mutableStateOf(false)
        private set
    var isConnected by mutableStateOf(false)
        private set
    var systemPath by mutableStateOf("")
        private set
    var macAddress by mutableStateOf("")
        private set
    var firmwareVersion by mutableStateOf("")
        private set

    init {
        isSearching = gatewayManager.isSearching.value
        isConnected = gatewayManager.currentGateway.value?.isConnected?.value == true
        updateGatewayDetails(gatewayManager.currentGateway.value)

        observeGatewayState()
        observeConnectionState()
        observeSearchingState()
    }

    private fun updateGatewayDetails(gateway: Gateway?) {
        if (gateway != null) {
            setGatewayDetails(gateway)
        } else {
            clearGatewayDetails()
        }
    }

    private fun clearGatewayDetails() {
        systemPath = ""
        macAddress = ""
        firmwareVersion = ""
    }

    private fun setGatewayDetails(gateway: Gateway) {
        systemPath = gateway.systemPath
        macAddress = gateway.macAddress
        firmwareVersion = gateway.firmwareVersion
    }

    private fun observeGatewayState() {
        gatewayManager.currentGateway.onEach { gateway ->
            updateGatewayDetails(gateway)
        }.launchIn(scope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeConnectionState() {
        gatewayManager.currentGateway
            .flatMapLatest { gateway ->
                gateway?.isConnected ?: flowOf(false)
            }
            .onEach { connected ->
                isConnected = connected
            }
            .launchIn(scope)
    }

    private fun observeSearchingState() {
        gatewayManager.isSearching.onEach { searching ->
            isSearching = searching
        }.launchIn(scope)
    }

    fun connect() {
        scope.launch(ioDispatcher) {
            try {
                gatewayManager.connect()
            } catch (e: Exception) {
                val message = e.message ?: "Failed to connect to gateway."
                errorHandler.show(message)
            }
        }
    }

    fun disconnect() {
        scope.launch(ioDispatcher) {
            try {
                gatewayManager.disconnect()
            } catch (e: Exception) {
                val message = e.message ?: "Failed to disconnect from gateway."
                errorHandler.show(message)
            }
        }
    }

}