package wrappers.serial

import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import logging.Logger
import serial.SerialFrameFormat
import serial.SerialPort
import java.io.IOException
import com.fazecast.jSerialComm.SerialPort as JSerialPort

class JSerialCommPort(
    override val name: String,
    override val baudRate: Int,
    override val frameFormat: SerialFrameFormat,
) : SerialPort {

    private val port: JSerialPort = JSerialPort.getCommPort(name)
    override val isOpen: Boolean get() = port.isOpen

    private val _incomingBytes = MutableSharedFlow<ByteArray>(extraBufferCapacity = 64)
    override val incomingBytes: SharedFlow<ByteArray> = _incomingBytes

    // Init
    init {
        port.baudRate = baudRate
        port.numDataBits = frameFormat.dataBits.toJSerialComm()
        port.numStopBits = frameFormat.stopBits.toJSerialComm()
        port.parity = frameFormat.parity.toJSerialComm()

        port.setComPortTimeouts(JSerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0)

        startPortListener()
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
        port.addDataListener(object : SerialPortDataListener {
            override fun getListeningEvents(): Int {
                return JSerialPort.LISTENING_EVENT_DATA_AVAILABLE or
                        JSerialPort.LISTENING_EVENT_PORT_DISCONNECTED
            }

            override fun serialEvent(event: SerialPortEvent) {
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
            val success = _incomingBytes.tryEmit(buffer.copyOf(bytesRead))
            if (!success) Logger.warning("Port $name failed to emit $bytesRead bytes.")
        }
    }

    // Open
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
    }

    // Close
    override suspend fun close() = withContext(Dispatchers.IO) {
        if (!isOpen) return@withContext

        if (port.closePort()) {
            Logger.debug("Port $name closed.")
        } else {
            Logger.warning("Port $name failed to close.")
        }
    }

    // Write
    override suspend fun write(data: ByteArray) = withContext(Dispatchers.IO) {
        check(isOpen) { "Port $name is not open." }

        val bytesWritten = port.writeBytes(data, data.size)

        if (bytesWritten != data.size) {
            throw IOException("Port $name wrote $bytesWritten of ${data.size} bytes.")
        }
    }

}