package server

import com.fazecast.jSerialComm.SerialPort

object UsbDeviceFinder {
    fun findEsp32Port(): String? {


        val ports = SerialPort.getCommPorts()

        ports.forEach {
            println("Available port: ${it.systemPortName}")
        }


        return ports
            .map { it.systemPortName }
            .firstOrNull { it.contains("usbserial", ignoreCase = true) && it.startsWith("cu.") }
    }
}