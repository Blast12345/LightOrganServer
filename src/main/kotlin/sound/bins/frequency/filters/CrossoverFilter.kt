package sound.bins.frequency.filters

import extensions.between
import math.featureScaling.normalize
import sound.bins.frequency.FrequencyBin
import sound.bins.frequency.FrequencyBinList
import kotlin.math.abs

// TODO: Normalize with a curve instead of linear?
class CrossoverFilter {

    fun filter(frequencyBinList: FrequencyBinList, crossover: Crossover): FrequencyBinList {
        return frequencyBinList.map {
            getFilteredBin(it, crossover)
        }
    }

    private fun getFilteredBin(frequencyBin: FrequencyBin, crossover: Crossover): FrequencyBin {
        return FrequencyBin(
            frequency = frequencyBin.frequency,
            magnitude = getFilteredMagnitude(frequencyBin, crossover)
        )
    }

    private fun getFilteredMagnitude(frequencyBin: FrequencyBin, crossover: Crossover): Float {
        val crossoverValue = getCrossoverValue(frequencyBin, crossover)
        return frequencyBin.magnitude * crossoverValue
    }

    private fun getCrossoverValue(frequencyBin: FrequencyBin, crossover: Crossover): Float {
        val normalizedFrequency = getNormalizedFrequency(frequencyBin, crossover)
        val attenuation = normalizedFrequency.between(0F, 1F)
        return abs(attenuation - 1)
    }

    private fun getNormalizedFrequency(frequencyBin: FrequencyBin, crossover: Crossover): Float {
        return getNormalizedFrequency(frequencyBin.frequency, crossover)
    }

    private fun getNormalizedFrequency(frequency: Float, crossover: Crossover): Float {
        return frequency.normalize(
            minimum = crossover.cornerFrequency,
            maximum = crossover.stopFrequency
        )
    }

}
