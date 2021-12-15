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
}