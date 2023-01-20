package sound.frequencyBins.dominantFrequency.frequency

import sound.frequencyBins.FrequencyBinList

interface TotalMagnitudeCalculatorInterface {
    fun calculate(frequencyBins: FrequencyBinList): Float
}

class TotalMagnitudeCalculator : TotalMagnitudeCalculatorInterface {

    override fun calculate(frequencyBins: FrequencyBinList): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

}