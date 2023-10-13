package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import kotlin.math.ln
import kotlin.math.max

// TODO: Find a more natural filter?
// The linear rolloff may lead to unusual averaging results
class BandPassFilter {

    fun filter(
        frequencyBinList: FrequencyBinList,
        lowCrossover: Crossover,
        highCrossover: Crossover
    ): FrequencyBinList {
        return frequencyBinList
            .removeBinsLessThan(lowCrossover.stopFrequency)
            .removeBinsGreaterThan(highCrossover.stopFrequency)
            .applyFilter(lowCrossover)
            .applyFilter(highCrossover)
    }

    private fun FrequencyBinList.removeBinsLessThan(frequency: Float): FrequencyBinList {
        return this.filter { it.frequency > frequency }
    }

    private fun FrequencyBinList.removeBinsGreaterThan(frequency: Float): FrequencyBinList {
        return this.filter { it.frequency < frequency }
    }

    private fun FrequencyBinList.applyFilter(filter: Crossover): FrequencyBinList {
        val filteredBins = mutableListOf<FrequencyBin>()

        for (bin in this) {
            val logPosition = log(bin.frequency) - log(filter.cornerFrequency)
            val logWidth = log(filter.stopFrequency) - log(filter.cornerFrequency)
            val multiplier = max(logPosition / logWidth, 0F)
            val magnitudeDecrease = bin.magnitude * multiplier
            val filteredMagnitude = bin.magnitude - magnitudeDecrease
            filteredBins.add(FrequencyBin(bin.frequency, filteredMagnitude))
        }

        return filteredBins
    }

    private fun log(float: Float): Float {
        val double = float.toDouble()
        val log = ln(double)
        return log.toFloat()
    }

}