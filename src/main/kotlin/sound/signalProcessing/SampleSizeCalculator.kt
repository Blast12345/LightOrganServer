package sound.signalProcessing

interface SampleSizeCalculatorInterface {
    fun calculate(frequency: Float, sampleRate: Float): Int
}

class SampleSizeCalculator : SampleSizeCalculatorInterface {

    override fun calculate(frequency: Float, sampleRate: Float): Int {
        val exactSampleSize = getExactSampleSize(frequency, sampleRate)
        return exactSampleSize.toInt()
    }

    private fun getExactSampleSize(frequency: Float, sampleRate: Float): Float {
        val secondsOfAudioRequired = getSecondsOfAudioRequired(frequency)
        return secondsOfAudioRequired * sampleRate
    }

    private fun getSecondsOfAudioRequired(frequency: Float): Float {
        return 1 / frequency
    }

}