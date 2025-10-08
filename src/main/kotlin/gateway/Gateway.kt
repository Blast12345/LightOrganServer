package gateway

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import lightOrgan.LightOrganSubscriber
import wrappers.color.Color

class Gateway(
    private val keyword: String = "Gateway",
    private val healthCheckMS: Long = 1000
) : LightOrganSubscriber {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var device: UsbDevice? = null

    init {
        startMonitoringConnection()
    }

    private fun startMonitoringConnection() {
        scope.launch {
            while (isActive) {
                if (device == null) {
                    device = UsbDeviceFinder.find(keyword)
                }

                if (device?.isConnected() == false) {
                    device?.connect()
                }

                delay(healthCheckMS)
            }
        }
    }

    override fun new(color: Color) {
        val colorMessage = "${color.red},${color.green},${color.blue}"
        device?.send(colorMessage)
    }

}