package sound.frequencyBins.dominantFrequency

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.finders.FrequencyBinFinder
import sound.frequencyBins.finders.FrequencyBinFinderInterface
import sound.frequencyBins.interpolators.MagnitudeInterpolator
import sound.frequencyBins.interpolators.MagnitudeInterpolatorInterface

interface MagnitudeEstimatorInterface {
    fun estimate(frequency: Float, frequencyBins: FrequencyBinList): Float?
}

class MagnitudeEstimator(
    private val frequencyBinFinder: FrequencyBinFinderInterface = FrequencyBinFinder(),
    private val magnitudeInterpolator: MagnitudeInterpolatorInterface = MagnitudeInterpolator()
) : MagnitudeEstimatorInterface {

    override fun estimate(frequency: Float, frequencyBins: FrequencyBinList): Float? {
        val exactBin = getExactFrequencyBin(frequency, frequencyBins)

        if (exactBin != null) {
            return exactBin.magnitude
        }

        val lowerNeighbor = getLowerNeighbor(frequency, frequencyBins)
        val higherNeighbor = getHigherNeighbor(frequency, frequencyBins)

        return if (lowerNeighbor != null && higherNeighbor != null) {
            interpolate(frequency, lowerNeighbor, higherNeighbor)
        } else if (lowerNeighbor != null) {
            lowerNeighbor.magnitude
        } else if (higherNeighbor != null) {
            higherNeighbor.magnitude
        } else {
            null
        }
    }

    private fun getExactFrequencyBin(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBinFinder.findExact(frequency, frequencyBins)
    }

    private fun getLowerNeighbor(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBinFinder.findLowerNeighbor(frequency, frequencyBins)
    }

    private fun getHigherNeighbor(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBinFinder.findHigherNeighbor(frequency, frequencyBins)
    }

    private fun interpolate(frequency: Float, smallerBin: FrequencyBin, largerBin: FrequencyBin): Float {
        return magnitudeInterpolator.interpolate(frequency, smallerBin, largerBin)
    }

}

