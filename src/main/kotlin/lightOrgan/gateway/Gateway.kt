package lightOrgan.gateway

import color.StandardRgbColor
import jsonrpc.JsonRpcConnection
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt

interface Gateway {
    val details: GatewayDetails
    val isConnected: StateFlow<Boolean>

    suspend fun disconnect()
    suspend fun broadcastColor(color: StandardRgbColor)
}

// TODO: Test me
class JsonRpcGateway(
    override val details: GatewayDetails,
    private val connection: JsonRpcConnection,
) : Gateway {

    override val isConnected = connection.isConnected

    override suspend fun disconnect() {
        connection.disconnect()
    }

    override suspend fun broadcastColor(color: StandardRgbColor) {
        val params = BroadcastColorParams(
            r = (color.red.value * 255).roundToInt(),
            g = (color.green.value * 255).roundToInt(),
            b = (color.blue.value * 255).roundToInt(),
        )

        connection.sendNotification("broadcast-color", params)
    }

    // TODO: Where should this live?
    private data class BroadcastColorParams(val r: Int, val g: Int, val b: Int)

}