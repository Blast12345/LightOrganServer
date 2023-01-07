package sound.frequencyBins

interface MinimumFrequencyFinderInterface {
    fun find(frequencyBinList: FrequencyBinList): Float?
}

class MinimumFrequencyFinder : MinimumFrequencyFinderInterface {

    override fun find(frequencyBinList: FrequencyBinList): Float? {
        return if (frequencyBinList.isNotEmpty()) {
            getMinimumFrequency(frequencyBinList)
        } else {
            null
        }
    }

    private fun getMinimumFrequency(frequencyBinList: FrequencyBinList): Float {
        val frequencyBin = getMinimumFrequencyBin(frequencyBinList)
        return frequencyBin.frequency
    }

    private fun getMinimumFrequencyBin(frequencyBinList: FrequencyBinList): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBinList)
        return sortedFrequencyBins.first()
    }

    private fun sorted(frequencyBinList: FrequencyBinList): FrequencyBinList {
        return frequencyBinList.sortedBy { it.frequency }
    }

}