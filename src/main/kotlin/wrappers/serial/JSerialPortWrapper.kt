package wrappers.serial

import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import logging.Logger
import serial.SerialFrameFormat
import serial.SerialPort
import java.io.IOException
import com.fazecast.jSerialComm.SerialPort as JSerialPort

//class JSerialPortWrapper(
//    private val port: JSerialPort,
//    private val timeout: Duration = 250.milliseconds
//) : SerialPort {
//
//    override val portPath: String = port.systemPortPath
//    override val isOpen: Boolean get() = port.isOpen
//
//    init {
//        handleUnexpectedDisconnect()
//        configureTimeouts()
//    }
//
//    // Ensures that isOpen can be trusted.
//    // Implemented as per documentation: https://fazecast.github.io/jSerialComm/javadoc/com/fazecast/jSerialComm/SerialPort.html#isOpen()
//    private fun handleUnexpectedDisconnect() {
//        port.addDataListener(object : SerialPortDataListener {
//            override fun getListeningEvents(): Int {
//                return JSerialPort.LISTENING_EVENT_PORT_DISCONNECTED
//            }
//
//            override fun serialEvent(event: SerialPortEvent) {
//                Logger.warning("$portPath: Port unexpectedly disconnected.")
//                port.closePort()
//            }
//        })
//    }
//
//    private fun configureTimeouts() {
//        port.setComPortTimeouts(
//            JSerialPort.TIMEOUT_READ_BLOCKING or JSerialPort.TIMEOUT_WRITE_BLOCKING,
//            timeout.inWholeMilliseconds.toInt(),
//            timeout.inWholeMilliseconds.toInt()
//        )
//    }
//
//    override fun open(baudRate: Int, format: SerialFrameFormat) {
//        check(!port.isOpen) { "$portPath: Port is already open." }
//
//        configurePort(baudRate, format)
//
//        if (!port.openPort()) {
//            throw SerialPortException(portPath, "Failed to open port.")
//        }
//
//        if (!port.flushIOBuffers()) {
//            Logger.debug("$portPath: Failed to flush IO buffers.")
//        }
//
//        Logger.debug("$portPath: Port opened.")
//    }
//
//    private fun configurePort(baudRate: Int, format: SerialFrameFormat) {
//        port.baudRate = baudRate
//        port.numDataBits = format.dataBits
//        port.parity = format.parity.toJSerialPort()
//        port.numStopBits = format.stopBits.toJSerialPort()
//    }
//
//    private fun SerialFrameFormat.Parity.toJSerialPort(): Int = when (this) {
//        SerialFrameFormat.Parity.NONE -> JSerialPort.NO_PARITY
//        SerialFrameFormat.Parity.ODD -> JSerialPort.ODD_PARITY
//        SerialFrameFormat.Parity.EVEN -> JSerialPort.EVEN_PARITY
//        SerialFrameFormat.Parity.MARK -> JSerialPort.MARK_PARITY
//        SerialFrameFormat.Parity.SPACE -> JSerialPort.SPACE_PARITY
//    }
//
//    private fun SerialFrameFormat.StopBits.toJSerialPort(): Int = when (this) {
//        SerialFrameFormat.StopBits.ONE -> JSerialPort.ONE_STOP_BIT
//        SerialFrameFormat.StopBits.TWO -> JSerialPort.TWO_STOP_BITS
//    }
//
//    override fun close() {
//        if (!port.isOpen) return
//
//        if (!port.closePort()) {
//            throw SerialPortException(portPath, "Failed to close port.")
//        }
//
//        Logger.debug("$portPath: Port closed.")
//    }
//
//    // TODO: duplicates being received by ESP32; is it on server side or gateway side?
//    override suspend fun writeLine(line: String) {
//        if (!port.isOpen) throw SerialPortException(portPath, "Port is not open.")
//
//        val line = line + "\n"
//        val bytes = line.toByteArray(Charsets.UTF_8)
//        val written = port.writeBytes(bytes, bytes.size)
//
//        if (written == -1) {
//            throw SerialPortException(portPath, "Failed to write line.")
//        }
//    }
//
//    // TODO: Make blocking
//    private val newLineByte = '\n'.code.toByte()
//    override suspend fun readNextLine(): String {
//        if (!port.isOpen) throw SerialPortException(portPath, "Port is not open.")
//
//        val buffer = ByteArrayOutputStream()
//
//        while (true) {
//            val nextByte = readNextByte()
//            if (nextByte == newLineByte) break
//            buffer.write(nextByte)
//        }
//
//        val bytes = buffer.toByteArray()
//
//        return String(bytes, Charsets.UTF_8)
//    }
//
//    private fun readNextByte(): Byte {
//        val buffer = ByteArray(1)
//        val bytesRead = port.readBytes(buffer, 1)
//        return buffer[0]
//    }
//
//    private fun ByteArrayOutputStream.write(byte: Byte) {
//        this.write(byte.toInt())
//    }
//
//}

class JSerialCommPort(
    override val name: String,
    override val baudRate: Int,
    override val frameFormat: SerialFrameFormat,
) : SerialPort {

    private val port: JSerialPort = JSerialPort.getCommPort(name)
    private val incomingBytesChannel = Channel<ByteArray>(Channel.BUFFERED)

    // Init
    init {
        port.setComPortParameters(
            baudRate,
            frameFormat.dataBits.toJSerialComm(),
            frameFormat.stopBits.toJSerialComm(),
            frameFormat.parity.toJSerialComm()
        )
    }

    private fun SerialFrameFormat.DataBits.toJSerialComm(): Int = when (this) {
        SerialFrameFormat.DataBits.FIVE -> 5
        SerialFrameFormat.DataBits.SIX -> 6
        SerialFrameFormat.DataBits.SEVEN -> 7
        SerialFrameFormat.DataBits.EIGHT -> 8
    }

    private fun SerialFrameFormat.Parity.toJSerialComm(): Int = when (this) {
        SerialFrameFormat.Parity.NONE -> JSerialPort.NO_PARITY
        SerialFrameFormat.Parity.ODD -> JSerialPort.ODD_PARITY
        SerialFrameFormat.Parity.EVEN -> JSerialPort.EVEN_PARITY
        SerialFrameFormat.Parity.MARK -> JSerialPort.MARK_PARITY
        SerialFrameFormat.Parity.SPACE -> JSerialPort.SPACE_PARITY
    }

    private fun SerialFrameFormat.StopBits.toJSerialComm(): Int = when (this) {
        SerialFrameFormat.StopBits.ONE -> JSerialPort.ONE_STOP_BIT
        SerialFrameFormat.StopBits.TWO -> JSerialPort.TWO_STOP_BITS
    }

    private fun startPortListener() {
        Logger.debug("Adding listener")

        port.addDataListener(object : SerialPortDataListener {
            override fun getListeningEvents(): Int {
                return JSerialPort.LISTENING_EVENT_DATA_AVAILABLE or
                        JSerialPort.LISTENING_EVENT_PORT_DISCONNECTED
            }

            override fun serialEvent(event: SerialPortEvent) {
                Logger.debug("Received event: ${event.eventType}")
                when (event.eventType) {
                    JSerialPort.LISTENING_EVENT_PORT_DISCONNECTED -> onDisconnect()
                    JSerialPort.LISTENING_EVENT_DATA_AVAILABLE -> onDataAvailable()
                }
            }
        })
    }

    private fun onDisconnect() {
        // Ensures that isOpen can be trusted.
        // https://fazecast.github.io/jSerialComm/javadoc/com/fazecast/jSerialComm/SerialPort.html#isOpen()
        Logger.warning("Port $name unexpectedly disconnected.")
        port.closePort()
    }

    private fun onDataAvailable() {
        val available = port.bytesAvailable()
        if (available <= 0) return

        val buffer = ByteArray(available)
        val bytesRead = port.readBytes(buffer, available)

        if (bytesRead > 0) {
            val sendResult = incomingBytesChannel.trySend(buffer.copyOf(bytesRead))
            if (sendResult.isFailure) Logger.warning("Port $name failed to emit $bytesRead bytes.")
        }
    }

    // Implementation
    override val isOpen: Boolean get() = port.isOpen
    override val incomingBytes: Flow<ByteArray> = incomingBytesChannel.receiveAsFlow()

    override suspend fun open() = withContext(Dispatchers.IO) {
        check(!isOpen) { "Port $name is already open." }

        if (port.openPort()) {
            Logger.debug("Port $name opened.")
        } else {
            throw IOException("Port $name failed to open.")
        }

        if (port.flushIOBuffers()) {
            Logger.debug("Port $name flushed IO buffers.")
        } else {
            Logger.debug("Port $name failed to flush IO buffers.")
        }

        startPortListener()
    }

    override suspend fun close() = withContext(Dispatchers.IO) {
        if (!isOpen) return@withContext

        if (port.closePort()) {
            Logger.debug("Port $name closed.")
        } else {
            Logger.warning("Port $name failed to close.")
        }
    }

    override suspend fun write(data: ByteArray) = withContext(Dispatchers.IO) {
        check(isOpen) { "Port $name is not open." }

        val bytesWritten = port.writeBytes(data, data.size)

        if (bytesWritten != data.size) {
            throw IOException("Port $name wrote $bytesWritten of ${data.size} bytes.")
        }
    }

}