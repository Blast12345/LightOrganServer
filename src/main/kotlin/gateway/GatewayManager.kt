package gateway

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// There is no present use case for more than one gateway.
// Additionally, it's important that concurrent searches are not allowed due to port lockouts.
class GatewayManager(
    private val gatewayFinder: GatewayFinder = GatewayFinder(),
    private val _isSearching: MutableStateFlow<Boolean> = MutableStateFlow(false),
    private val _currentGateway: MutableStateFlow<Gateway?> = MutableStateFlow(null),
//    private val scope: CoroutineScope
) {

    val isSearching = _isSearching.asStateFlow()
    val currentGateway = _currentGateway.asStateFlow()

    // TODO: Implement me after the rest of the Gateway stuff is working
//    private var connectionObserverJob: Job? = null
//    private var reconnectionJob: Job? = null
//
//    init {
//        observeGatewayConnection()
//    }
//
//    private fun observeGatewayConnection() {
//        scope.launch {
//
//            currentGateway.collect { gateway ->
//                connectionObserverJob?.cancel()
//                reconnectionJob?.cancel()
//
//                if (gateway != null) {
//
//                    connectionObserverJob = scope.launch {
//
//                        gateway.isConnected.collect { isConnected ->
//                            if (!isConnected) {
//                                attemptReconnection(gateway)
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
//    }
//
//    private fun attemptReconnection(gateway: Gateway) {
//        reconnectionJob?.cancel()
//
//        reconnectionJob = scope.launch {
//            while (true) {
//                try {
//                    gateway.connect()
//
//                    // If successful, break the loop
//                    if (gateway.isConnected.value) {
//                        break
//                    }
//
//                } catch (e: Exception) {
//                    // Connection failed, will retry after delay
//                }
//            }
//        }
//    }

    suspend fun connect() {
        if (currentGateway.value != null) {
            currentGateway.value?.reconnect() // TODO: Update test
            return
        }

        if (isSearching.value) {
            throw IllegalStateException("Already attempting to connect. Please wait.")
        }

        _isSearching.value = true
        _currentGateway.value = gatewayFinder.find() // TODO: Presume connected or not?
        _isSearching.value = false
    }

    fun disconnect() {
        _currentGateway.value?.disconnect()
        _currentGateway.value = null
    }

}