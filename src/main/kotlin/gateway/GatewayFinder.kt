package gateway

import gateway.serial.wrappers.SerialPortFinder

class GatewayFinder(
    private val serialPortFinder: SerialPortFinder = SerialPortFinder()
) {

    suspend fun find(): Gateway? {
        val allPorts = serialPortFinder.find()

        for (port in allPorts) {
            val gateway = runCatching {
                Gateway.connect(port)
            }.getOrNull()

            if (gateway != null) {
                return gateway
            }
        }

        return null
    }

}