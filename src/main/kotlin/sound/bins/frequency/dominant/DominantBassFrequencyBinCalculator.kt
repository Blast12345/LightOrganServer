package sound.bins.frequency.dominant

import dsp.fft.FrequencyBin
import dsp.fft.FrequencyBins
import sound.bins.frequency.dominant.frequency.DominantFrequencyCalculator
import sound.bins.frequency.dominant.magnitude.DominantMagnitudeCalculator

class DominantBassFrequencyBinCalculator(
    private val dominantFrequencyCalculator: DominantFrequencyCalculator = DominantFrequencyCalculator(),
    private val dominantMagnitudeCalculator: DominantMagnitudeCalculator = DominantMagnitudeCalculator(),
) {

    @Suppress("ReturnCount")
    fun calculate(frequencyBins: FrequencyBins): FrequencyBin? {
        val frequency = dominantFrequencyCalculator.calculate(frequencyBins) ?: return null
        val magnitude = dominantMagnitudeCalculator.calculate(frequencyBins) ?: return null

        return FrequencyBin(frequency, magnitude)
    }

}
