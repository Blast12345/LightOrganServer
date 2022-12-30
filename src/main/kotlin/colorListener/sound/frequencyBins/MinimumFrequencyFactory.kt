package colorListener.sound.frequencyBins

interface MinimumFrequencyFactoryInterface {
    fun minimumFrequencyFrom(frequencyBins: FrequencyBins): Float?
}

class MinimumFrequencyFactory : MinimumFrequencyFactoryInterface {

    override fun minimumFrequencyFrom(frequencyBins: FrequencyBins): Float? {
        return if (frequencyBins.isNotEmpty()) {
            val frequencyBin = minimumFrequencyBinFrom(frequencyBins)
            return frequencyBin.frequency.toFloat()
        } else {
            null
        }
    }

    private fun minimumFrequencyBinFrom(frequencyBins: FrequencyBins): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBins)
        return sortedFrequencyBins.first()
    }

    private fun sorted(frequencyBins: FrequencyBins): FrequencyBins {
        return frequencyBins.sortedBy { it.frequency }
    }

}