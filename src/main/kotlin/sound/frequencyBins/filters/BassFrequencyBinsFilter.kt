package sound.frequencyBins.filters

import config.Config
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

interface BassFrequencyBinsFilterInterface {
    fun filter(frequencyBins: FrequencyBinList): FrequencyBinList
}

class BassFrequencyBinsFilter(
    config: Config
) : BassFrequencyBinsFilterInterface {

    private val highPassFilter = config.highPassFilter
    private val highPassFrequency = highPassFilter.frequency
    private val rollOffRange = highPassFilter.rollOffRange
    private val highestFrequency = highPassFrequency + rollOffRange

    override fun filter(frequencyBins: FrequencyBinList): FrequencyBinList {
        val binsInRange = getFrequencyBinsInRange(frequencyBins)
        return applyRollOff(binsInRange)
    }

    private fun getFrequencyBinsInRange(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.filter { it.frequency <= highestFrequency }
    }

    private fun applyRollOff(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.map { frequencyBin ->
            val isAboveHighPassFrequency = frequencyBin.frequency > highPassFrequency

            if (isAboveHighPassFrequency) {
                getRolledOffFrequencyBin(frequencyBin)
            } else {
                frequencyBin
            }
        }
    }

    private fun getRolledOffFrequencyBin(frequencyBin: FrequencyBin): FrequencyBin {
        return FrequencyBin(
            frequency = frequencyBin.frequency,
            magnitude = getRolledOffMagnitude(frequencyBin)
        )
    }

    private fun getRolledOffMagnitude(frequencyBin: FrequencyBin): Float {
        return calculateLinearRollOff(
            magnitude = frequencyBin.magnitude,
            progress = getProgressInRollOff(frequencyBin.frequency)
        )
    }

    private fun getProgressInRollOff(frequency: Float): Float {
        val positionInRollOff = frequency - highPassFrequency
        return (positionInRollOff / rollOffRange)
    }

    private fun calculateLinearRollOff(magnitude: Float, progress: Float): Float {
        return magnitude * (1 - progress)
    }

}