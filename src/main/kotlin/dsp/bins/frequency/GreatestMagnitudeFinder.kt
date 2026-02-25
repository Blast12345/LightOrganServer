package dsp.bins.frequency

class GreatestMagnitudeFinder {

    fun find(frequencyBins: FrequencyBins): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}
