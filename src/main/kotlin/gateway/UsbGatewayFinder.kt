package gateway

import com.fazecast.jSerialComm.SerialPort

object UsbGatewayFinder {

    fun find(): UsbDevice? {
        val ports = SerialPort.getCommPorts()
        val port = ports.firstOrNull()
        return port?.let { UsbDevice(it) }
    }

}