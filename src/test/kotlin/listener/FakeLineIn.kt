package listener

import sound.LineIn

class FakeLineIn(private val sampleRate: Double,
                 private val sampleSize: Int,
                 private val frame: DoubleArray): LineIn {

    override fun getSampleRate(): Double {
        return sampleRate
    }

    override fun getSampleSize(): Int {
        return sampleSize
    }

    override fun getFrame(): DoubleArray {
        return frame
    }

    override fun availableBytes(): Int {
        TODO("Not yet implemented")
    }

    override fun bufferSize(): Int {
        TODO("Not yet implemented")
    }

    override fun bitDepth(): Int {
        TODO("Not yet implemented")
    }

    override fun hasDataAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isBigEndian(): Boolean {
        TODO("Not yet implemented")
    }

    override fun readAvailable(): ByteArray {
        TODO("Not yet implemented")
    }
}