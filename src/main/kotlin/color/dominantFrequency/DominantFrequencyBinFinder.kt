package color.dominantFrequency

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

interface DominantFrequencyBinFinderInterface {
    fun find(frequencyBins: FrequencyBinList): FrequencyBin
}

class DominantFrequencyBinFinder : DominantFrequencyBinFinderInterface {

    override fun find(frequencyBins: FrequencyBinList): FrequencyBin {
        TODO("Not yet implemented")
    }

}