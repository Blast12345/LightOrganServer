package sound.frequencyBins

interface MaximumFrequencyFinderInterface {
    fun find(frequencyBins: FrequencyBins): Float?
}

class MaximumFrequencyFinder : MaximumFrequencyFinderInterface {

    override fun find(frequencyBins: FrequencyBins): Float? {
        return if (frequencyBins.isNotEmpty()) {
            getMaximumFrequency(frequencyBins)
        } else {
            null
        }
    }

    private fun getMaximumFrequency(frequencyBins: FrequencyBins): Float {
        val frequencyBin = getMaximumFrequencyBin(frequencyBins)
        return frequencyBin.frequency
    }

    private fun getMaximumFrequencyBin(frequencyBins: FrequencyBins): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBins)
        return sortedFrequencyBins.last()
    }

    private fun sorted(frequencyBins: FrequencyBins): FrequencyBins {
        return frequencyBins.sortedBy { it.frequency }
    }

}