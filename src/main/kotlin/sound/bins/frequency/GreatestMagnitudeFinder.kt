package sound.bins.frequency

import dsp.bins.FrequencyBins

class GreatestMagnitudeFinder {

    fun find(frequencyBins: FrequencyBins): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}
