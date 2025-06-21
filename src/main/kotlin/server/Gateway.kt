package server

import com.fazecast.jSerialComm.SerialPort

class Gateway {
    private val baudRate: Int = 115200
    private val port: SerialPort

    init {
        val portDescriptor = UsbDeviceFinder.findEsp32Port()

        if (portDescriptor == null) {
            throw IllegalStateException("No ESP32 USB device found.")
        }

        port = SerialPort.getCommPort("cu.usbserial-0001")
        port.baudRate = baudRate

        if (port.openPort()) {
            println("Port opened successfully")
        } else {
            println("Failed to open port")
        }
    }

    fun send(message: String) {
        println("Sending message: $message")
        val bytes = (message + "\n").toByteArray()
        port.writeBytes(bytes, bytes.size)
        port.flushIOBuffers()
    }

    fun close() {
        port.closePort()
    }
}