package gateway

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import lightOrgan.LightOrganSubscriber
import wrappers.color.Color

class Gateway(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    private val healthCheckMS: Long = 1000
) : LightOrganSubscriber {

    private var currentDevice: UsbDevice? = null

    fun setUsbDevice(newDevice: UsbDevice?) {
        disconnectIfNeeded()

        currentDevice = newDevice
        startMaintainingConnection()
    }

    private fun disconnectIfNeeded() {
        if (currentDevice?.isConnected() == true) {
            stopHealthCheck()
            currentDevice?.disconnect()
        }
    }

    private fun stopHealthCheck() {
        scope.cancel()
    }

    private fun startMaintainingConnection() {
        scope.launch {
            while (isActive) {
                if (currentDevice?.isConnected() == false) {
                    println("USB Device is not connected. Attempting to connect...")
                    currentDevice?.connect()
                }

                delay(healthCheckMS)
            }
        }
    }

    override fun new(color: Color) {
        val colorMessage = "${color.red},${color.green},${color.blue}"
        currentDevice?.send(colorMessage)
    }

}