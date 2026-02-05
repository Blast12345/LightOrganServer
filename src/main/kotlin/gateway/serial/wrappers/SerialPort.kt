package gateway.serial.wrappers

import wrappers.Wrapper
import java.io.ByteArrayOutputStream
import com.fazecast.jSerialComm.SerialPort as JSerialPort

@Wrapper
interface SerialPort {
    val path: String
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

    private val newLineByte = '\n'.code.toByte()

    override val path: String = port.systemPortPath

    override fun open(baudRate: Int, format: SerialFormat) {
        port.baudRate = baudRate
        port.numDataBits = format.bits
        port.parity = format.parity
        port.numStopBits = format.stopBits

        port.setComPortTimeouts(JSerialPort.TIMEOUT_WRITE_BLOCKING, timeoutMs, timeoutMs)
        port.setComPortTimeouts(JSerialPort.TIMEOUT_READ_BLOCKING, timeoutMs, timeoutMs)

        if (!port.openPort()) {
            throw SerialPortException(path, "Failed to open port.")
        }

        if (!port.flushIOBuffers()) {
            throw SerialPortException(path, "Failed to flush buffers.")
        }
    }

    override fun close() {
        if (!port.closePort()) {
            throw SerialPortException(path, "Failed to close port.")
        }
    }

    override fun writeLine(data: String) {
        val line = data + "\n"
        val bytes = line.toByteArray(Charsets.UTF_8)
        val written = port.writeBytes(bytes, bytes.size)

        if (written == -1) {
            throw SerialPortException(path, "Failed to write line.")
        }
    }

    override fun readNextLine(): String? {
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