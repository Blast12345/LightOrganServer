package sound.frequencyBins.finders

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

interface FrequencyBinFinderInterface {
    fun findExact(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin?
    fun findLowerNeighbor(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin?
    fun findHigherNeighbor(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin?
    fun findLowest(frequencyBins: FrequencyBinList): FrequencyBin?
    fun findHighest(frequencyBins: FrequencyBinList): FrequencyBin?
}

class FrequencyBinFinder : FrequencyBinFinderInterface {

    override fun findExact(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBins.firstOrNull { it.frequency == frequency }
    }

    override fun findLowerNeighbor(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBins.lastOrNull { it.frequency < frequency }
    }

    override fun findHigherNeighbor(frequency: Float, frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBins.firstOrNull { it.frequency > frequency }
    }

    override fun findLowest(frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBins.minByOrNull { it.frequency }
    }

    override fun findHighest(frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBins.maxByOrNull { it.frequency }
    }

}
