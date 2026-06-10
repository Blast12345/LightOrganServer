package serial

import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import toolkit.monkeyTest.nextInt
import toolkit.monkeyTest.nextSerialFrameFormat
import toolkit.monkeyTest.nextString

class FakeSerialPort : SerialPort {

    override val name: String = nextString("name")
    override val baudRate: Int = nextInt()
    override val frameFormat: SerialFrameFormat = nextSerialFrameFormat()
    override val isOpen = MutableStateFlow(false)

    override suspend fun open() {
        isOpen.value = true
    }

    override suspend fun close() {
        isOpen.value = false
    }

    // Write
    var suspendWrites = false
    val writtenBytes = mutableListOf<ByteArray>()
    val writtenLines: List<String> get() = writtenBytes.map { String(it).trim() }

    override suspend fun write(data: ByteArray) {
        if (suspendWrites) awaitCancellation()
        writtenBytes.add(data)
    }

    // Read
    override val incomingBytes = MutableSharedFlow<ByteArray>()

    suspend fun receiveLine(json: String) {
        incomingBytes.emit((json + "\n").toByteArray())
    }

}