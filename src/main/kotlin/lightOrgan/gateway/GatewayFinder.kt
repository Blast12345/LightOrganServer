package lightOrgan.gateway

import wrappers.serial.SerialPortFinder

// OPTIMIZATION: Prioritize last known path
class GatewayFinder(
    private val serialPortFinder: SerialPortFinder = SerialPortFinder(),
    private val serialClientFactory: SerialClientFactory = SerialClientFactory(),
    private val serialGatewayConnector: SerialGatewayConnector = SerialGatewayConnector(),
) {

    suspend fun find(): Gateway? {
        val allPorts = serialPortFinder.find()

        for (port in allPorts) {
            val client = serialClientFactory.create(port)
            val gateway = serialGatewayConnector.connect(client)
            if (gateway != null) return gateway
        }

        return null
    }

}