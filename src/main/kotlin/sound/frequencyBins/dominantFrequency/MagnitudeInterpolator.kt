package sound.frequencyBins.dominantFrequency

import sound.frequencyBins.FrequencyBin

interface MagnitudeInterpolatorInterface {
    fun interpolate(frequency: Float, binA: FrequencyBin, binB: FrequencyBin): Float
}

class MagnitudeInterpolator : MagnitudeInterpolatorInterface {

    override fun interpolate(frequency: Float, binA: FrequencyBin, binB: FrequencyBin): Float {
        val stepSize = getStepSize(binA, binB)
        val relativePositionOfFrequency = getRelativePositionOfFrequency(frequency, binA)
        return (stepSize * relativePositionOfFrequency) + binA.magnitude
    }

    private fun getStepSize(binA: FrequencyBin, binB: FrequencyBin): Float {
        val magnitudeRange = getMagnitudeDifference(binA, binB)
        val frequencyRange = getFrequencyDifference(binA, binB)
        return getStepSize(magnitudeRange, frequencyRange)
    }

    private fun getFrequencyDifference(binA: FrequencyBin, binB: FrequencyBin): Float {
        return binA.frequency - binB.frequency
    }

    private fun getMagnitudeDifference(binA: FrequencyBin, binB: FrequencyBin): Float {
        return binA.magnitude - binB.magnitude
    }

    private fun getStepSize(magnitudeRange: Float, frequencyRange: Float): Float {
        return magnitudeRange / frequencyRange
    }

    private fun getRelativePositionOfFrequency(frequency: Float, binA: FrequencyBin): Float {
        return frequency - binA.frequency
    }

}
