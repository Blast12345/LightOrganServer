package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import kotlin.math.ln
import kotlin.math.max

class CrossoverFilter {

    fun filter(frequencyBinList: FrequencyBinList, crossover: Crossover): FrequencyBinList {
        return frequencyBinList
            .removeOutOfBandBins(crossover)
            .applyRollOff(crossover)
    }

    private fun FrequencyBinList.removeOutOfBandBins(crossover: Crossover): FrequencyBinList {
        return this.filter {
            checkIfBinIsInBand(it, crossover)
        }
    }

    private fun checkIfBinIsInBand(frequencyBin: FrequencyBin, crossover: Crossover): Boolean {
        return getMultiplier(frequencyBin, crossover) <= 1F
    }

    private fun getMultiplier(frequencyBin: FrequencyBin, crossover: Crossover): Float {
        val logPosition = ln(frequencyBin.frequency) - ln(crossover.cornerFrequency)
        val logWidth = ln(crossover.stopFrequency) - ln(crossover.cornerFrequency)
        return max(logPosition / logWidth, 0F)
    }

    private fun FrequencyBinList.applyRollOff(crossover: Crossover): FrequencyBinList {
        return this.map {
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
        val multiplier = getMultiplier(frequencyBin, crossover)
        val magnitudeDecrease = frequencyBin.magnitude * multiplier
        return frequencyBin.magnitude - magnitudeDecrease
    }

}