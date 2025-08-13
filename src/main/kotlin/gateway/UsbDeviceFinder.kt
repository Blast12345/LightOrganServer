package gateway

import com.fazecast.jSerialComm.SerialPort

object UsbDeviceFinder {

    fun find(): UsbDevice? {
        val ports = SerialPort.getCommPorts()
        val port = ports.firstOrNull { it.descriptivePortName.contains("Gateway") }
        return port?.let { UsbDevice(it) }
    }

}