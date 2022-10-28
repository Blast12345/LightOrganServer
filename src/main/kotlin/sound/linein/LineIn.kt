package sound

interface LineIn {
    fun getSampleRate(): Double
    fun getSampleSize(): Int
    fun bitDepth(): Int
    fun isBigEndian(): Boolean
    fun bufferSize(): Int
    fun getFrame(): DoubleArray
    fun availableBytes(): Int
    fun hasDataAvailable(): Boolean // return lineIn.available() > 0
    fun readAvailable(): ByteArray

//    val availableBytes = lineIn.availableBytes()
//    val newData = ByteArray(availableBytes)
//    val readBytes = lineIn.read(newData, 0, newData.size)
}