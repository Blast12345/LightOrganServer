package color

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

interface MaximumFrequencyFinderInterface {
    fun find(frequencyBins: FrequencyBinList): Float?
}

class MaximumFrequencyFinder : MaximumFrequencyFinderInterface {

    override fun find(frequencyBins: FrequencyBinList): Float? {
        return if (frequencyBins.isNotEmpty()) {
            getMaximumFrequency(frequencyBins)
        } else {
            null
        }
    }

    private fun getMaximumFrequency(frequencyBins: FrequencyBinList): Float {
        val frequencyBin = getMaximumFrequencyBin(frequencyBins)
        return frequencyBin.frequency
    }

    private fun getMaximumFrequencyBin(frequencyBins: FrequencyBinList): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBins)
        return sortedFrequencyBins.last()
    }

    private fun sorted(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.sortedBy { it.frequency }
    }

}