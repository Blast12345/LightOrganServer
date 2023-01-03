package sound.frequencyBins

import kotlin.math.pow

interface SampleSizeFactoryInterface {
    fun createFor(frequency: Float, sampleRate: Float): Int
}

class SampleSizeFactory : SampleSizeFactoryInterface {

    override fun createFor(frequency: Float, sampleRate: Float): Int {
        val exactSampleSize = getExactSampleSizeFor(frequency, sampleRate)
        val power = getPower(exactSampleSize)
        return 2.0.pow(power).toInt()
    }

    private fun getPower(exactSampleSize: Float): Int {
        var power = 0

        while (2.0.pow(power) < exactSampleSize) {
            power += 1
        }

        return power
    }

    private fun getExactSampleSizeFor(frequency: Float, sampleRate: Float): Float {
        val secondsOfAudioRequired = getSecondsOfAudioRequiredFor(frequency)
        return secondsOfAudioRequired * sampleRate
    }

    private fun getSecondsOfAudioRequiredFor(frequency: Float): Float {
        return 1 / frequency
    }

}