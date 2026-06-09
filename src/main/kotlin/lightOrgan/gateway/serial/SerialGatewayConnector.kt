package lightOrgan.gateway.serial

import jsonrpc.sendRequest
import lightOrgan.gateway.Gateway
import lightOrgan.gateway.GatewayIdentificationResponse
import lightOrgan.gateway.JsonRpcGateway
import lightOrgan.gateway.SerialGatewayDetails
import logging.Logger
import serial.SerialPort
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SerialGatewayConnector {

    // TODO: Should timeout be configurable?
    suspend fun connect(port: SerialPort, timeout: Duration = 100.milliseconds): Gateway {
        val connection = SerialJsonRpcConnection(port)
        connection.connect()

        try {
            val response: GatewayIdentificationResponse = connection.sendRequest("gateway-identification-request", null, timeout)

            Logger.success("Port ${port.name} handshake successful.")

            return JsonRpcGateway(
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