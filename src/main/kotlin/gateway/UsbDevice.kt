package gateway

import com.fazecast.jSerialComm.SerialPort

class UsbDevice(
    private val port: SerialPort
) {

    fun connect() {
        port.openPort()
    }

    fun disconnect() {
        port.closePort()
    }

    fun isConnected(): Boolean {
        return port.isOpen
    }

    fun isNotConnected(): Boolean {
        return !port.isOpen
    }

    fun send(message: String) {
        println("Sending message: $message")
        val bytes = (message + "\n").toByteArray()
        port.writeBytes(bytes, bytes.size)
        port.flushIOBuffers()
    }

}