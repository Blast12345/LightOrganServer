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

}