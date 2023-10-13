package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import kotlin.math.ln
import kotlin.math.max

// TODO: Test me
class CrossoverFilter {

    fun filter(frequencyBinList: FrequencyBinList, crossover: Crossover): FrequencyBinList {
        val filteredFrequencyBinList = mutableListOf<FrequencyBin>()

        for (bin in frequencyBinList) {
            val logPosition = ln(bin.frequency) - ln(crossover.cornerFrequency)
            val logWidth = ln(crossover.stopFrequency) - ln(crossover.cornerFrequency)
            val multiplier = max(logPosition / logWidth, 0F)
            val magnitudeDecrease = bin.magnitude * multiplier
            val filteredMagnitude = bin.magnitude - magnitudeDecrease

            val inPassBand = multiplier < 1F
            if (inPassBand) {
                filteredFrequencyBinList.add(FrequencyBin(bin.frequency, filteredMagnitude))
            }
        }

        return filteredFrequencyBinList
    }

}