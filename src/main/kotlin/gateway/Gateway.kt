package gateway

import com.fazecast.jSerialComm.SerialPort
import lightOrgan.LightOrganSubscriber
import wrappers.color.Color

class Gateway(
    private val port: SerialPort
//    private val device: UsbDevice,
//    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
//    private val healthCheckMS: Long = 1000,
) : LightOrganSubscriber {

    init {
//        startMaintainingConnection()
    }

    //    private fun startMaintainingConnection() {
//        scope.launch {
//            while (isActive) {
//                if (device.isNotConnected()) {
//                    println("USB Device is not connected. Attempting to connect...")
//                    device.connect()
//                }
//
//                delay(healthCheckMS)
//            }
//        }
//    }
//
    override fun new(color: Color) {
//        if (device.isNotConnected()) {
//            return
//        }
//
//        val colorMessage = "${color.red},${color.green},${color.blue}"
//        device.send(colorMessage)
    }

}