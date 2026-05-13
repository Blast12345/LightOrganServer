package sound.bins.frequency.dominant

import dsp.bins.FrequencyBin
import dsp.bins.FrequencyBins
import org.apache.commons.math3.complex.Complex
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


        return FrequencyBin(
            frequency,
            Complex(magnitude.toDouble(), 0.0) // This  is a shortcut that will not be relevant after the color refactor
        )
    }

}
