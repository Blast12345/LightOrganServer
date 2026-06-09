package serial

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import toolkit.monkeyTest.nextInt
import toolkit.monkeyTest.nextSerialFrameFormat
import toolkit.monkeyTest.nextString

class FakeSerialPort : SerialPort {
    override val name: String = nextString("name")
    override val baudRate: Int = nextInt()
    override val frameFormat: SerialFrameFormat = nextSerialFrameFormat()
    override val isOpen: StateFlow<Boolean> = MutableStateFlow(false)
    override val incomingBytes: Flow<ByteArray> = MutableStateFlow(ByteArray(0))

    override suspend fun open() {
        TODO("Not yet implemented")
    }

    override suspend fun close() {
        TODO("Not yet implemented")
    }

    override suspend fun write(data: ByteArray) {
        TODO("Not yet implemented")
    }
}