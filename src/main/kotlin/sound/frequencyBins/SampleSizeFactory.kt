package sound.frequencyBins

import kotlin.math.pow

interface SampleSizeFactoryInterface {
    fun create(frequency: Float, sampleRate: Float): Int
}

class SampleSizeFactory : SampleSizeFactoryInterface {

    override fun create(frequency: Float, sampleRate: Float): Int {
        val power = getPower(frequency, sampleRate)
        return 2.0.pow(power).toInt()
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