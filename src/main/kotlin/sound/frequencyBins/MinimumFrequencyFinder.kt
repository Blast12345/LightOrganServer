package sound.frequencyBins

interface MinimumFrequencyFinderInterface {
    fun find(frequencyBins: FrequencyBins): Float?
}

class MinimumFrequencyFinder : MinimumFrequencyFinderInterface {

    override fun find(frequencyBins: FrequencyBins): Float? {
        return if (frequencyBins.isNotEmpty()) {
            getMinimumFrequency(frequencyBins)
        } else {
            null
        }
    }

    private fun getMinimumFrequency(frequencyBins: FrequencyBins): Float {
        val frequencyBin = getMinimumFrequencyBin(frequencyBins)
        return frequencyBin.frequency
    }

    private fun getMinimumFrequencyBin(frequencyBins: FrequencyBins): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBins)
        return sortedFrequencyBins.first()
    }

    private fun sorted(frequencyBins: FrequencyBins): FrequencyBins {
        return frequencyBins.sortedBy { it.frequency }
    }

}