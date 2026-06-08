package lightOrgan.gateway

import color.StandardRgbColor
import jsonrpc.JsonRpcConnection
import jsonrpc.sendRequest
import kotlinx.coroutines.flow.StateFlow
import logging.Logger
import serial.SerialFrameFormat
import serial.SerialPort
import kotlin.math.roundToInt
import kotlin.time.Duration

interface Gateway {
    val details: Details
    val isConnected: StateFlow<Boolean>

    suspend fun disconnect()
    suspend fun broadcastColor(color: StandardRgbColor)

    // Types
    sealed interface Details {
        val macAddress: String
        val firmwareVersion: String
    }
}

// TODO: Test me
class RealGateway private constructor(
    override val details: Gateway.Details,
    private val connection: JsonRpcConnection,
) : Gateway {

    companion object {
        suspend fun connect(port: SerialPort, timeout: Duration): Gateway {
            val connection = SerialJsonRpcConnection(port)
            connection.connect()

            try {
                val response: GatewayIdentificationResponse = connection.sendRequest("gateway-identification-request", null, timeout)

                Logger.success("Port ${port.name} handshake successful.")

                return RealGateway(
                    details = SerialGatewayDetails(
                        macAddress = response.macAddress,
                        firmwareVersion = response.firmwareVersion,
                        baudRate = connection.baudRate,
                        frameFormat = connection.frameFormat
                    ),
                    connection = connection
                )
            } catch (e: Exception) {
                connection.disconnect()
                throw e
            }
        }
    }

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

data class SerialGatewayDetails(
    override val macAddress: String,
    override val firmwareVersion: String,
    val baudRate: Int,
    val frameFormat: SerialFrameFormat,
) : Gateway.Details