package sound.bins.frequency

class GreatestMagnitudeFinder {

    fun find(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}
