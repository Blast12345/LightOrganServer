package sound.bins.frequency

class GreatestMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}