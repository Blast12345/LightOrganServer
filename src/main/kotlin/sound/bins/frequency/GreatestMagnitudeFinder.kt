package sound.bins.frequency

import bins.frequency.FrequencyBins

class GreatestMagnitudeFinder {

    fun find(frequencyBins: FrequencyBins): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}
