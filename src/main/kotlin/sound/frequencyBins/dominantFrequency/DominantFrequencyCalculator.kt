package sound.frequencyBins.dominantFrequency

import sound.frequencyBins.FrequencyBinList


interface DominantFrequencyCalculatorInterface {
    fun calculate(frequencyBins: FrequencyBinList): Float?
}

// TODO: Test me
class DominantFrequencyCalculator : DominantFrequencyCalculatorInterface {

    // ENHANCEMENT: Introduce an averaging algorithm if we can clean up the smeared data
    override fun calculate(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.maxByOrNull { it.magnitude }?.frequency
    }

}