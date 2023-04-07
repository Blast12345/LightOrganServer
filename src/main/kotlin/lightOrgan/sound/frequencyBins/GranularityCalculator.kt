package lightOrgan.sound.frequencyBins

interface GranularityCalculatorInterface {
    fun calculate(numberOfBins: Int, sampleRate: Float, numberOfChannels: Int): Float
}

class GranularityCalculator(
    private val nyquistFrequencyCalculator: NyquistFrequencyCalculatorInterface = NyquistFrequencyCalculator()
) : GranularityCalculatorInterface {

    override fun calculate(numberOfBins: Int, sampleRate: Float, numberOfChannels: Int): Float {
        val nyquistFrequency = calculateNyquistFrequency(sampleRate)
        return nyquistFrequency / numberOfBins * numberOfChannels
    }

    private fun calculateNyquistFrequency(sampleRate: Float): Float {
        return nyquistFrequencyCalculator.calculate(sampleRate)
    }

}