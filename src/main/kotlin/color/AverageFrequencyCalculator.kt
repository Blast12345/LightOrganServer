package color

import sound.frequencyBins.FrequencyBinList

interface AverageFrequencyCalculatorInterface {
    fun calculate(frequencyBins: FrequencyBinList): Float?
}

class AverageFrequencyCalculator : AverageFrequencyCalculatorInterface {

    override fun calculate(frequencyBins: FrequencyBinList): Float? {
        return if (frequencyBins.isNotEmpty()) {
            val weightedMagnitude = weightedMagnitude(frequencyBins)
            val totalMagnitude = totalMagnitude(frequencyBins)
            return averageFrequency(weightedMagnitude, totalMagnitude)
        } else {
            null
        }
    }

    private fun averageFrequency(weightedMagnitude: Float, totalMagnitude: Float): Float? {
        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
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

}