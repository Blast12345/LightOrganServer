package lightOrgan.gateway

import kotlinx.coroutines.CompletableDeferred

class FakeGatewayFinder : GatewayFinder {

    var gateway = FakeGateway()
    var error: Exception? = null

    override suspend fun find(): Gateway {
        pause?.await()
        error?.let { throw it }
        return gateway
    }

    // Helpers
    private var pause: CompletableDeferred<Unit>? = null

    fun pause() {
        pause = CompletableDeferred()
    }

    fun resume() {
        pause?.complete(Unit)
        pause = null
    }


}