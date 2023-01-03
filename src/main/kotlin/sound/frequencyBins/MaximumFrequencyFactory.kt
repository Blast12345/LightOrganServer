package sound.frequencyBins

interface MaximumFrequencyFactoryInterface {
    fun maximumFrequency(frequencyBins: FrequencyBins): Float?
}

class MaximumFrequencyFactory : MaximumFrequencyFactoryInterface {

    override fun maximumFrequency(frequencyBins: FrequencyBins): Float? {
        return if (frequencyBins.isNotEmpty()) {
            val frequencyBin = maximumFrequencyBin(frequencyBins)
            return frequencyBin.frequency.toFloat()
        } else {
            null
        }
    }

    private fun maximumFrequencyBin(frequencyBins: FrequencyBins): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBins)
        return sortedFrequencyBins.last()
    }

    private fun sorted(frequencyBins: FrequencyBins): FrequencyBins {
        return frequencyBins.sortedBy { it.frequency }
    }

}