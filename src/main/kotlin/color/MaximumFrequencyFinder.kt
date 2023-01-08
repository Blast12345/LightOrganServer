package color

import sound.frequencyBins.FrequencyBinList

interface MaximumFrequencyFinderInterface {
    fun find(frequencyBins: FrequencyBinList): Float?
}

class MaximumFrequencyFinder : MaximumFrequencyFinderInterface {

    override fun find(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.maxOfOrNull { it.frequency }
    }

}