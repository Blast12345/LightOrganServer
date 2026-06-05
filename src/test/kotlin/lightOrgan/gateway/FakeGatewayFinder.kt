package lightOrgan.gateway

import kotlinx.coroutines.CompletableDeferred

class FakeGatewayFinder : GatewayFinder {

    var gateway = FakeGateway()
    var deferFind: CompletableDeferred<Unit>? = null
    var error: Exception? = null

    override suspend fun find(): Gateway {
        error?.let { throw it }
        deferFind?.await()
        return gateway
    }

}