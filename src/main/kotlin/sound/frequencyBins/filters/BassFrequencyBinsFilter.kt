package sound.frequencyBins.filters

import ConfigSingleton
import config.Config
import config.children.HighPassFilter
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

class BassFrequencyBinsFilter(
    private val config: Config = ConfigSingleton
) {

    fun filter(frequencyBins: FrequencyBinList): FrequencyBinList {
        val binsInRange = getFrequencyBinsInRange(frequencyBins)
        return applyRollOff(binsInRange)
    }

    private fun getFrequencyBinsInRange(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.filter { it.frequency <= highestFrequency }
    }

    private val highestFrequency: Float
        get() = highPassFrequency + rollOffRange

    private val highPassFrequency: Float
        get() = highPassFilter.frequency

    private val rollOffRange: Float
        get() = highPassFilter.rollOffRange

    private val highPassFilter: HighPassFilter
        get() = config.highPassFilter

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