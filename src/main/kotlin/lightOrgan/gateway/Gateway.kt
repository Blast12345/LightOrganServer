package lightOrgan.gateway

import color.StandardRgbColor
import jsonrpc.JsonRpcPeer
import jsonrpc.SerialJsonRpcPeer
import jsonrpc.sendRequest
import lightOrgan.gateway.serial.messages.GatewayIdentificationResponse
import serial.SerialFrameFormat
import serial.SerialPort
import kotlin.time.Duration

// TODO: Test me
class Gateway private constructor(
    val details: GatewayDetails,
    private val device: JsonRpcPeer
) {

    companion object {
        suspend fun connect(port: SerialPort, timeout: Duration): Gateway {
            val device = SerialJsonRpcPeer(port)
            device.connect()

            try {
                val response: GatewayIdentificationResponse = device.sendRequest("gateway-identification-request", null, timeout)

                return Gateway(
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

    suspend fun disconnect() {
        device.disconnect()
    }

    suspend fun broadcastColor(color: StandardRgbColor) {
        device.sendNotification("broadcast-color", color)
    }

}

sealed interface GatewayDetails {
    val macAddress: String
    val firmwareVersion: String
}


data class SerialGatewayDetails(
    override val macAddress: String,
    override val firmwareVersion: String,
    val baudRate: Int,
    val frameFormat: SerialFrameFormat,
) : GatewayDetails