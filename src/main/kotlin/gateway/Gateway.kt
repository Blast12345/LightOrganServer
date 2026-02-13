package gateway

import gateway.messages.GatewayIdentificationResponse
import gateway.messages.MessageFactory
import gateway.serial.SerialRouter
import gateway.serial.wrappers.SerialFormat
import gateway.serial.wrappers.SerialPort
import wrappers.color.Color

class Gateway private constructor(
    val systemPath: String,
    val macAddress: String,
    val firmwareVersion: String,
    private val serialRouter: SerialRouter,
    private val messageFactory: MessageFactory
) {

    val isConnected = serialRouter.isConnected

    companion object {

        // Hardcoded for now; may introduce config later
        val baudRate: Int = 115200
        val serialFormat: SerialFormat = SerialFormat.FORMAT_8N1

        suspend fun connect(
            port: SerialPort,
            messageFactory: MessageFactory = MessageFactory()
        ): Gateway {
            val serialRouter = SerialRouter.create(port, baudRate, serialFormat)

            try {
                serialRouter.connect()

                val response = handshake(serialRouter, messageFactory)

                return Gateway(
                    port.systemPath,
                    response.macAddress,
                    response.firmwareVersion,
                    serialRouter,
                    messageFactory
                )
            } catch (e: Exception) {
                serialRouter.disconnect()
                throw e
            }
        }

        private suspend fun handshake(
            serialRouter: SerialRouter,
            requestFactory: MessageFactory
        ): GatewayIdentificationResponse {
            val request = requestFactory.createIdentificationRequest()
            return serialRouter.send(request, GatewayIdentificationResponse::class.java)
        }

    }

    fun reconnect() {
        serialRouter.connect()
    }

    fun disconnect() {
        serialRouter.disconnect()
    }

    fun send(color: Color) {
        val command = messageFactory.createColorCommand(color)
        serialRouter.send(command)
    }

}