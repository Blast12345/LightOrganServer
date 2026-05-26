package lightOrgan.gateway

import wrappers.serial.SerialPortFinder

// ENHANCEMENT: Prioritize last known path
class GatewayFinder(
    private val serialPortFinder: SerialPortFinder = SerialPortFinder(),
    private val serialGatewayConnector: SerialGatewayConnector = SerialGatewayConnector()
) {

    suspend fun find(): Gateway? {
        val allPorts = serialPortFinder.find()

        for (port in allPorts) {
            val gateway = serialGatewayConnector.connect(port)
            if (gateway != null) return gateway
        }

        return null
    }

}
