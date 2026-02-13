package gateway.serial.wrappers

import toolkit.monkeyTest.nextString

class FakeSerialPort : SerialPort {

    override var isOpen: Boolean = false
    override val systemPath: String = nextString("path")

    // State for testing
    var openException: Exception? = null
    var baudRate: Int? = null
    var format: SerialFormat? = null
    var closeException: Exception? = null
    val responseMap = mutableMapOf<String, List<String>>()
    val writtenLines = mutableListOf<String>()
    val linesToRead = mutableListOf<String>()
    // State for testing

    override fun open(baudRate: Int, format: SerialFormat) {
        this.baudRate = baudRate
        this.format = format

        if (openException != null) throw openException as Throwable

        isOpen = true
    }

    override fun close() {
        if (closeException != null) throw closeException as Throwable

        isOpen = false
    }

    override fun writeLine(data: String) {
        if (!isOpen) throw Exception("Port is not open.")

        writtenLines.add(data)

        responseMap[data]?.let { lines ->
            linesToRead.addAll(lines)
        }
    }

    override fun readNextLine(): String? {
        if (!isOpen) throw Exception("Port is not open.")

        return linesToRead.removeFirstOrNull()
    }

}