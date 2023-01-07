package sound.frequencyBins

interface GranularityCalculatorInterface {
    fun calculate(numberOfBins: Int, sampleRate: Float): Float
}

class GranularityCalculator(
    private val nyquistFrequencyCalculator: NyquistFrequencyCalculatorInterface = NyquistFrequencyCalculator()
) : GranularityCalculatorInterface {

    override fun calculate(numberOfBins: Int, sampleRate: Float): Float {
        val nyquistFrequency = calculateNyquistFrequency(sampleRate)
        return nyquistFrequency / numberOfBins
    }

    private fun calculateNyquistFrequency(sampleRate: Float): Float {
        return nyquistFrequencyCalculator.calculate(sampleRate)
    }

}