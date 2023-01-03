package sound.frequencyBins

interface MinimumFrequencyFactoryInterface {
    fun minimumFrequency(frequencyBins: FrequencyBins): Float?
}

class MinimumFrequencyFactory : MinimumFrequencyFactoryInterface {

    override fun minimumFrequency(frequencyBins: FrequencyBins): Float? {
        return if (frequencyBins.isNotEmpty()) {
            val frequencyBin = minimumFrequencyBin(frequencyBins)
            return frequencyBin.frequency.toFloat()
        } else {
            null
        }
    }

    private fun minimumFrequencyBin(frequencyBins: FrequencyBins): FrequencyBin {
        val sortedFrequencyBins = sorted(frequencyBins)
        return sortedFrequencyBins.first()
    }

    private fun sorted(frequencyBins: FrequencyBins): FrequencyBins {
        return frequencyBins.sortedBy { it.frequency }
    }

}