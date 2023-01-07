package color

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

interface MinimumFrequencyFinderInterface {
    fun find(frequencyBins: FrequencyBinList): Float?
}

class MinimumFrequencyFinder : MinimumFrequencyFinderInterface {

    override fun find(frequencyBins: FrequencyBinList): Float? {
        return if (frequencyBins.isNotEmpty()) {
            getMinimumFrequency(frequencyBins)
        } else {
            null
        }
    }

    private fun getMinimumFrequency(frequencyBins: FrequencyBinList): Float {
        val frequencyBin = getMinimumFrequencyBin(frequencyBins)
        return frequencyBin.frequency
    }

    private fun getMinimumFrequencyBin(frequencyBins: FrequencyBinList): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBins)
        return sortedFrequencyBins.first()
    }

    private fun sorted(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.sortedBy { it.frequency }
    }

}