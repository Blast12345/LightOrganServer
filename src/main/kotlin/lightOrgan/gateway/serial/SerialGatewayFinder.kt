package lightOrgan.gateway.serial

import kotlinx.coroutines.TimeoutCancellationException
import lightOrgan.gateway.Gateway
import lightOrgan.gateway.GatewayFinder
import logging.Logger
import serial.SerialPortFinder
import wrappers.serial.JSerialPortFinder
import kotlin.coroutines.cancellation.CancellationException

// OPTIMIZATION: Prioritize last known path
class SerialGatewayFinder(
    private val portFinder: SerialPortFinder = JSerialPortFinder(),
    private val connector: SerialGatewayConnector = SerialGatewayConnector()
) : GatewayFinder {

    override suspend fun find(): Gateway {
        val ports = portFinder.find()

        for (port in ports) {
            try {
                return connector.connect(port)
            } catch (e: TimeoutCancellationException) {
                Logger.error("Port ${port.name} connection attempt timed out.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Logger.error("Port ${port.name} failed to connect.", e)
            }
        }

        throw IllegalStateException("No gateway was found.")
    }

}