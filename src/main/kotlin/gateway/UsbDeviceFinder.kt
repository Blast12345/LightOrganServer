package gateway

import com.fazecast.jSerialComm.SerialPort

object UsbDeviceFinder {

    fun find(keyword: String): UsbDevice? {
        val ports = SerialPort.getCommPorts()
        val port = ports.firstOrNull { it.descriptivePortName.contains(keyword) }
        return port?.let { UsbDevice(it) }
    }

}