package color

import sound.frequencyBins.FrequencyBinList

interface MinimumFrequencyFinderInterface {
    fun find(frequencyBins: FrequencyBinList): Float?
}

class MinimumFrequencyFinder : MinimumFrequencyFinderInterface {

    override fun find(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.minOfOrNull { it.frequency }
    }

}