package sound.frequencyBins

class GreatestMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}