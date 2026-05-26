package lightOrgan.gateway

import annotations.SkipCoverage
import color.StandardRgbColor
import kotlinx.coroutines.flow.StateFlow

data class GatewayDetails(
    val systemPath: String,
    val macAddress: String,
    val firmwareVersion: String
)

interface Gateway {
    val details: GatewayDetails
    val isConnected: StateFlow<Boolean>
    fun connect()
    fun disconnect()
    fun sendColor(color: StandardRgbColor)
}


@SkipCoverage
class SerialGatewayFactory {

    fun create(
        identity: GatewayIdentificationResponse,
        client: SerialClient
    ): SerialGateway {
        return SerialGateway(
            details = GatewayDetails(
                systemPath = client.systemPath,
                macAddress = identity.macAddress,
                firmwareVersion = identity.firmwareVersion,
            ),
            serialClient = client
        )
    }

}

class SerialGateway(
    override val details: GatewayDetails,
    private val serialClient: SerialClient
) : Gateway {

    override val isConnected = serialClient.isConnected

    override fun connect() {
        serialClient.connect()
    }

    override fun disconnect() {
        serialClient.disconnect()
    }

    override fun sendColor(color: StandardRgbColor) {
        TODO()
    }

}


//    val isConnected = serialRouter.isConnected
//
//    companion object {
//
//        // Hardcoded for now; may introduce config later
//        val baudRate: Int = 115200
//        val serialFormat: SerialFormat = SerialFormat.FORMAT_8N1
//
//        suspend fun connect(
//            port: SerialPort,
//            messageFactory: MessageFactory = MessageFactory()
//        ): Gateway {
//            val serialRouter = SerialRouter.create(port, baudRate, serialFormat)
//
//            try {
//                serialRouter.connect()
//
//                val response = handshake(serialRouter, messageFactory)
//
//                return Gateway(
//                    port.systemPath,
//                    response.macAddress,
//                    response.firmwareVersion,
//                    serialRouter,
//                    messageFactory
//                )
//            } catch (e: Exception) {
//                serialRouter.disconnect()
//                throw e
//            }
//        }
//
//        private suspend fun handshake(
//            serialRouter: SerialRouter,
//            requestFactory: MessageFactory
//        ): GatewayIdentificationResponse {
//            val request = requestFactory.createIdentificationRequest()
//            return serialRouter.send(request, GatewayIdentificationResponse::class.java)
//        }
//
//    }
//