package colorListener.sound.frequencyBins

interface AverageFrequencyFactoryInterface {
    fun averageFrequencyFrom(frequencyBins: FrequencyBins): Float?
}

class AverageFrequencyFactory : AverageFrequencyFactoryInterface {

    override fun averageFrequencyFrom(frequencyBins: FrequencyBins): Float? {
        return if (frequencyBins.isNotEmpty()) {
            val weightedAmplitude = weightedAmplitudeFrom(frequencyBins)
            val totalAmplitude = totalAmplitudeFrom(frequencyBins)
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

    private fun weightedAmplitudeFrom(frequencyBins: FrequencyBins): Float {
        var weightedAmplitude = 0.0

        for (frequencyBin in frequencyBins) {
            weightedAmplitude += frequencyBin.frequency * frequencyBin.amplitude
        }

        return weightedAmplitude.toFloat()
    }

    private fun totalAmplitudeFrom(frequencyBins: FrequencyBins): Float {
        return frequencyBins.map { it.amplitude }.sum().toFloat()
    }

}