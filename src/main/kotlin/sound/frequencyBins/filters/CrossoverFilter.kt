package sound.frequencyBins.filters

import extensions.between
import math.LogarithmicMinMaxNormalization
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import kotlin.math.abs

class CrossoverFilter(
    private val logarithmicMinMaxNormalization: LogarithmicMinMaxNormalization = LogarithmicMinMaxNormalization()
) {

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
        return logarithmicMinMaxNormalization.calculate(
            value = frequencyBin.frequency,
            minimum = crossover.cornerFrequency,
            maximum = crossover.stopFrequency,
            base = 2F
        )
    }

}