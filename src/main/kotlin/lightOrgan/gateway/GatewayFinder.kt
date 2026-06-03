package lightOrgan.gateway

import kotlinx.coroutines.TimeoutCancellationException
import logging.Logger
import serial.SerialPortFinder
import wrappers.serial.JSerialPortFinder
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

// OPTIMIZATION: Prioritize last known path
class GatewayFinder(
    private val serialPortFinder: SerialPortFinder = JSerialPortFinder()
) {

    suspend fun find(): Gateway? {
        val ports = serialPortFinder.find()

        for (port in ports) {
            try {
                // TODO: Should timeout be configurable?
                return Gateway.connect(port, 100.milliseconds)
            } catch (e: TimeoutCancellationException) {
                Logger.error(e.message ?: "Port ${port.name} failed to connect.", e)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Logger.error(e.message ?: "Port ${port.name} failed to connect.", e)
            }
        }

        return null
    }

}