package sound.frequencyBins

class GranularityCalculator(
    private val nyquistFrequencyCalculator: NyquistFrequencyCalculator = NyquistFrequencyCalculator()
) {

    fun calculate(numberOfBins: Int, sampleRate: Float, numberOfChannels: Int): Float {
        val nyquistFrequency = calculateNyquistFrequency(sampleRate)
        return nyquistFrequency / numberOfBins * numberOfChannels
    }

    private fun calculateNyquistFrequency(sampleRate: Float): Float {
        return nyquistFrequencyCalculator.calculate(sampleRate)
    }

}