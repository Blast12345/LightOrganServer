package colorService.sound

class FrequencyBins(private val frequencyBins: List<FrequencyBin>) {

    fun minimumFrequency(): Double {
        return frequencyBins.first().frequency
    }

    fun maximumFrequency(): Double {
        return frequencyBins.last().frequency
    }

    fun averageFrequency(): Double {
        // TODO: Handle case of 0 as total amplitude
        return weightedAmplitude() / totalAmplitude()
    }

    private fun weightedAmplitude(): Double {
        var weightedAmplitude = 0.0

        for (frequencyBin in frequencyBins) {
            weightedAmplitude += frequencyBin.frequency * frequencyBin.amplitude
        }

        return weightedAmplitude
    }

    private fun totalAmplitude(): Double {
        return frequencyBins.map { it.amplitude }.sum()
    }

}