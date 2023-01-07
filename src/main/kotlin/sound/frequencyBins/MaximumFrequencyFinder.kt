package sound.frequencyBins

interface MaximumFrequencyFinderInterface {
    fun find(frequencyBinList: FrequencyBinList): Float?
}

class MaximumFrequencyFinder : MaximumFrequencyFinderInterface {

    override fun find(frequencyBinList: FrequencyBinList): Float? {
        return if (frequencyBinList.isNotEmpty()) {
            getMaximumFrequency(frequencyBinList)
        } else {
            null
        }
    }

    private fun getMaximumFrequency(frequencyBinList: FrequencyBinList): Float {
        val frequencyBin = getMaximumFrequencyBin(frequencyBinList)
        return frequencyBin.frequency
    }

    private fun getMaximumFrequencyBin(frequencyBinList: FrequencyBinList): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBinList)
        return sortedFrequencyBins.last()
    }

    private fun sorted(frequencyBinList: FrequencyBinList): FrequencyBinList {
        return frequencyBinList.sortedBy { it.frequency }
    }

}