package gateway.serial.wrappers

import annotations.Wrapper
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import java.io.ByteArrayOutputStream
import com.fazecast.jSerialComm.SerialPort as JSerialPort

@Wrapper
interface SerialPort {
    val systemPath: String
    val isOpen: Boolean
    fun open(baudRate: Int, format: SerialFormat)
    fun close()
    fun writeLine(data: String)
    fun readNextLine(): String?
}

@Wrapper
class SerialPortWrapper(
    private val port: JSerialPort,
    private val timeoutMs: Int = 250
) : SerialPort {

    override val systemPath: String = port.systemPortPath
    override val isOpen: Boolean
        get() = port.isOpen
    private val newLineByte = '\n'.code.toByte()

    init {
        handleDisconnects()
    }

    private fun handleDisconnects() {
        // Ensures that isOpen can be trusted.
        // Implemented as per documentation: https://fazecast.github.io/jSerialComm/javadoc/com/fazecast/jSerialComm/SerialPort.html#isOpen()
        port.addDataListener(object : SerialPortDataListener {
            override fun getListeningEvents(): Int {
                return JSerialPort.LISTENING_EVENT_PORT_DISCONNECTED
            }

            override fun serialEvent(event: SerialPortEvent) {
                if (event.eventType == JSerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
                    port.closePort()
                }
            }
        })
    }

    override fun open(baudRate: Int, format: SerialFormat) {
        port.baudRate = baudRate
        port.numDataBits = format.bits
        port.parity = format.parity
        port.numStopBits = format.stopBits

        port.setComPortTimeouts(JSerialPort.TIMEOUT_WRITE_BLOCKING, timeoutMs, timeoutMs)
        port.setComPortTimeouts(JSerialPort.TIMEOUT_READ_BLOCKING, timeoutMs, timeoutMs)

        if (!port.openPort()) {
            throw SerialPortException(systemPath, "Failed to open port.")
        }

        if (!port.flushIOBuffers()) {
            throw SerialPortException(systemPath, "Failed to flush buffers.")
        }
    }

    override fun close() {
        if (!port.closePort()) {
            throw SerialPortException(systemPath, "Failed to close port.")
        }
    }

    // TODO: duplicates being received by ESP32; is it on server side or gateway side?
    override fun writeLine(data: String) {
        if (!port.isOpen) throw SerialPortException(systemPath, "Port is not open.")

        val line = data + "\n"
        val bytes = line.toByteArray(Charsets.UTF_8)
        val written = port.writeBytes(bytes, bytes.size)

        if (written == -1) {
            throw SerialPortException(systemPath, "Failed to write line.")
        }
    }

    override fun readNextLine(): String? {
        if (!port.isOpen) throw SerialPortException(systemPath, "Port is not open.")

        val buffer = ByteArrayOutputStream()

        while (true) {
            val nextByte = readNextByte() ?: return null
            if (nextByte == newLineByte) break
            buffer.write(nextByte)
        }

        val bytes = buffer.toByteArray()

        return if (bytes.isNotEmpty()) {
            String(bytes, Charsets.UTF_8)
        } else {
            null
        }
    }

    private fun readNextByte(): Byte? {
        val buffer = ByteArray(1)
        val bytesRead = port.readBytes(buffer, 1)

        return if (bytesRead > 0) buffer[0] else null
    }

    private fun ByteArrayOutputStream.write(byte: Byte) {
        this.write(byte.toInt())
    }

}