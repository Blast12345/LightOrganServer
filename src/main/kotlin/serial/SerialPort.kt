package serial

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SerialPort {
    val name: String
    val baudRate: Int
    val frameFormat: SerialFrameFormat

    val isOpen: StateFlow<Boolean>
    val incomingBytes: Flow<ByteArray>

    suspend fun open()
    suspend fun close()

    // WARNING: A mutex or equivalent should be used to prevent concurrent writes from interleaving data
    suspend fun write(data: ByteArray)
}

