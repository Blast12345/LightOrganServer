package lightOrgan.gateway

import kotlinx.coroutines.TimeoutCancellationException
import logging.Logger
import serial.SerialPortFinder
import wrappers.serial.JSerialPortFinder
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

interface GatewayFinder {
    suspend fun find(): Gateway
}

// OPTIMIZATION: Prioritize last known path
class RealGatewayFinder(
    private val serialPortFinder: SerialPortFinder = JSerialPortFinder()
) : GatewayFinder {

    override suspend fun find(): Gateway {
        val ports = serialPortFinder.find()

        for (port in ports) {
            try {
                // TODO: Should timeout be configurable?
                return RealGateway.connect(port, 100.milliseconds)
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