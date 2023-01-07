package color

import sound.frequencyBins.FrequencyBinList

interface AverageFrequencyCalculatorInterface {
    fun calculate(frequencyBinList: FrequencyBinList): Float?
}

class AverageFrequencyCalculator : AverageFrequencyCalculatorInterface {

    override fun calculate(frequencyBinList: FrequencyBinList): Float? {
        return if (frequencyBinList.isNotEmpty()) {
            val weightedMagnitude = weightedMagnitude(frequencyBinList)
            val totalMagnitude = totalMagnitude(frequencyBinList)
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

    private fun weightedMagnitude(frequencyBinList: FrequencyBinList): Float {
        var weightedMagnitude = 0F

        for (frequencyBin in frequencyBinList) {
            weightedMagnitude += frequencyBin.frequency * frequencyBin.magnitude
        }

        return weightedMagnitude
    }

    private fun totalMagnitude(frequencyBinList: FrequencyBinList): Float {
        return frequencyBinList.map { it.magnitude }.sum()
    }

}