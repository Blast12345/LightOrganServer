package sound.linein

import sound.LineIn

class TargetLineIn(/*private val targetDataLine: TargetDataLine*/): LineIn {

    override fun getSampleRate(): Double {
//        return targetDataLine.format.sampleRate.toDouble()
        return 500.0
    }

    override fun getSampleSize(): Int {
        // TODO: Pass in sample size calculator
        return 1
    }

    override fun availableBytes(): Int {
        TODO("Not yet implemented")
    }

    override fun bitDepth(): Int {
        TODO("Not yet implemented")
    }

    override fun bufferSize(): Int {
        TODO("Not yet implemented")
    }

    override fun isBigEndian(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getFrame(): DoubleArray {
        TODO("Not yet implemented")
    }

    override fun hasDataAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun readAvailable(): ByteArray {
        TODO("Not yet implemented")
    }

}