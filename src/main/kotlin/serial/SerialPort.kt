package serial

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class SerialPort {
    // Details
    abstract val name: String
    abstract val baudRate: Int
    abstract val frameFormat: SerialFrameFormat

    // Life cycle
    abstract val isOpen: StateFlow<Boolean>

    abstract suspend fun open()
    abstract suspend fun close()

    // Read
    abstract val incomingBytes: Flow<ByteArray>

    // Write
    private val writeMutex = Mutex()

    suspend fun write(data: ByteArray) {
        writeMutex.withLock { writeRaw(data) }
    }

    protected abstract suspend fun writeRaw(data: ByteArray)
}