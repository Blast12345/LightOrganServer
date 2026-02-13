package gateway

import gateway.serial.wrappers.SerialPortFinder

class GatewayFinder(
    private val serialPortFinder: SerialPortFinder = SerialPortFinder()
) {

    // TODO: There should only be a single find operation GLOBALLY happening at a time; how to enforce?
    // TODO: Prioritize last known path
    suspend fun find(): Gateway? {
        val allPorts = serialPortFinder.find()

        for (port in allPorts) {
            val gateway = runCatching {
                Gateway.connect(port)
            }.getOrNull()

            if (gateway != null) {
//                gateway.disconnect() // TODO: Be a good little boyscout?
                return gateway
            }
        }

        return null
    }

}