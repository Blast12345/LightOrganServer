package colorListener.sound.frequencyBins

interface MaximumFrequencyFactoryInterface {
    fun maximumFrequencyFrom(frequencyBins: FrequencyBins): Float?
}

class MaximumFrequencyFactory : MaximumFrequencyFactoryInterface {

    override fun maximumFrequencyFrom(frequencyBins: FrequencyBins): Float? {
        return if (frequencyBins.isNotEmpty()) {
            val frequencyBin = maximumFrequencyBinFrom(frequencyBins)
            return frequencyBin.frequency.toFloat()
        } else {
            null
        }
    }

    private fun maximumFrequencyBinFrom(frequencyBins: FrequencyBins): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBins)
        return sortedFrequencyBins.last()
    }

    private fun sorted(frequencyBins: FrequencyBins): FrequencyBins {
        return frequencyBins.sortedBy { it.frequency }
    }

}