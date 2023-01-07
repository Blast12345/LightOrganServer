package sound.signalProcessing

import kotlin.math.pow

interface SampleSizeCalculatorInterface {
    fun calculate(frequency: Float, sampleRate: Float): Int
}

class SampleSizeCalculator : SampleSizeCalculatorInterface {

    override fun calculate(frequency: Float, sampleRate: Float): Int {
        val power = getPower(frequency, sampleRate)
        val sampleSize = 2.0.pow(power)
        return sampleSize.toInt()
    }

    private fun getPower(frequency: Float, sampleRate: Float): Int {
        var power = 0
        val exactSampleSize = getExactSampleSize(frequency, sampleRate)

        while (2.0.pow(power) < exactSampleSize) {
            power += 1
        }

        return power
    }

    private fun getExactSampleSize(frequency: Float, sampleRate: Float): Float {
        val secondsOfAudioRequired = getSecondsOfAudioRequired(frequency)
        return secondsOfAudioRequired * sampleRate
    }

    private fun getSecondsOfAudioRequired(frequency: Float): Float {
        return 1 / frequency
    }

}