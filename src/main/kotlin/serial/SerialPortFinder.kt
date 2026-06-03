package serial

interface SerialPortFinder {
    fun find(): List<SerialPort>
}