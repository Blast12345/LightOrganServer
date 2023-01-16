package sound.frequencyBins.dominantFrequency

import sound.frequencyBins.FrequencyBinList

interface DominantFrequencyCalculatorInterface {
    fun calculate(frequencyBins: FrequencyBinList): Float?
}

class DominantFrequencyCalculator : DominantFrequencyCalculatorInterface {

    override fun calculate(frequencyBins: FrequencyBinList): Float? {
        val weightedMagnitude = weightedMagnitude(frequencyBins)
        val totalMagnitude = totalMagnitude(frequencyBins)
        return averageFrequency(weightedMagnitude, totalMagnitude)
    }

    private fun weightedMagnitude(frequencyBins: FrequencyBinList): Float {
        var weightedMagnitude = 0F

        for (frequencyBin in frequencyBins) {
            weightedMagnitude += frequencyBin.frequency * frequencyBin.magnitude
        }

        return weightedMagnitude
    }

    private fun totalMagnitude(frequencyBins: FrequencyBinList): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

    private fun averageFrequency(weightedMagnitude: Float, totalMagnitude: Float): Float? {
        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
    }

}