package lightOrgan.gateway

import color.StandardRgbColor
import jsonrpc.JsonRpcConnection
import jsonrpc.sendRequest
import kotlinx.coroutines.flow.StateFlow
import logging.Logger
import serial.SerialFrameFormat
import serial.SerialPort
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
    private val device: JsonRpcConnection,
) : Gateway {

    companion object {
        suspend fun connect(port: SerialPort, timeout: Duration): Gateway {
            val device = SerialJsonRpcJsonRpcConnection(port)
            device.connect()

            try {
                val response: GatewayIdentificationResponse = device.sendRequest("gateway-identification-request", null, timeout)

                Logger.success("Port ${port.name} handshake successful.")

                return RealGateway(
                    details = SerialGatewayDetails(
                        macAddress = response.macAddress,
                        firmwareVersion = response.firmwareVersion,
                        baudRate = device.baudRate,
                        frameFormat = device.frameFormat
                    ),
                    device = device
                )
            } catch (e: Exception) {
                device.disconnect()
                throw e
            }
        }
    }

    override val isConnected = device.isConnected

    override suspend fun disconnect() {
        device.disconnect()
    }

    override suspend fun broadcastColor(color: StandardRgbColor) {
        device.sendNotification("broadcast-color", color)
    }

}

data class SerialGatewayDetails(
    override val macAddress: String,
    override val firmwareVersion: String,
    val baudRate: Int,
    val frameFormat: SerialFrameFormat,
) : Gateway.Details