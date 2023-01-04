package sound.frequencyBins

interface AverageFrequencyFactoryInterface {
    fun averageFrequency(frequencyBins: FrequencyBins): Float?
}

class AverageFrequencyFactory : AverageFrequencyFactoryInterface {

    override fun averageFrequency(frequencyBins: FrequencyBins): Float? {
        return if (frequencyBins.isNotEmpty()) {
            val weightedAmplitude = weightedAmplitude(frequencyBins)
            val totalAmplitude = totalAmplitude(frequencyBins)
            return averageFrequency(weightedAmplitude, totalAmplitude)
        } else {
            null
        }
    }

    private fun averageFrequency(weightedAmplitude: Float, totalAmplitude: Float): Float? {
        return if (totalAmplitude == 0F) {
            null
        } else {
            weightedAmplitude / totalAmplitude
        }
    }

    private fun weightedAmplitude(frequencyBins: FrequencyBins): Float {
        var weightedAmplitude = 0.0

        for (frequencyBin in frequencyBins) {
            weightedAmplitude += frequencyBin.frequency * frequencyBin.amplitude
        }

        return weightedAmplitude.toFloat()
    }

    private fun totalAmplitude(frequencyBins: FrequencyBins): Float {
        return frequencyBins.map { it.amplitude }.sum().toFloat()
    }

}