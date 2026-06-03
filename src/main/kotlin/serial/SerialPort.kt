package serial

import kotlinx.coroutines.flow.Flow

interface SerialPort {
    val name: String
    val baudRate: Int
    val frameFormat: SerialFrameFormat

    val isOpen: Boolean
    val incomingBytes: Flow<ByteArray>

    suspend fun open()
    suspend fun close()
    suspend fun write(data: ByteArray)
}

