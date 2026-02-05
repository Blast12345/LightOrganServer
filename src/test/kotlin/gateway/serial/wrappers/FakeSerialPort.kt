package gateway.serial.wrappers

import toolkit.monkeyTest.nextString

class FakeSerialPort : SerialPort {

    // State for testing
    var isOpen: Boolean = false
    var baudRate: Int? = null
    var format: SerialFormat? = null

    val responseMap = mutableMapOf<String, List<String>>()
    val linesToRead = mutableListOf<String>()

    // Fake implementation
    override val path: String = nextString("path") // TODO: Is this used?

    override fun open(baudRate: Int, format: SerialFormat) {
        if (isOpen) throw Exception("Port is already open")
        
        isOpen = true
        this.baudRate = baudRate
        this.format = format
    }

    override fun close(): Unit {
        isOpen = false
    }

    override fun writeLine(data: String) {
        if (!isOpen) throw Exception("Port is not open.")

        responseMap[data]?.let { lines ->
            linesToRead.addAll(lines)
        }
    }

    override fun readNextLine(): String? {
        if (!isOpen) throw Exception("Port is not open.")

        return linesToRead.removeFirstOrNull()
    }

}