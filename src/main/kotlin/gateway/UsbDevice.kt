package gateway

import com.fazecast.jSerialComm.SerialPort

class UsbDevice(
    private val port: SerialPort
) {

    fun send(message: String) {
        println("Sending message: $message")
        val bytes = (message + "\n").toByteArray()
        port.writeBytes(bytes, bytes.size)
        port.flushIOBuffers()
    }

}