package color

import dsp.fft.FrequencyBins
import sound.bins.frequency.GreatestMagnitudeFinder

class BrightnessCalculator(
    private val greatestMagnitudeFinder: GreatestMagnitudeFinder = GreatestMagnitudeFinder(),
) {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        // TODO: How to scale?
        val magnitude = greatestMagnitudeFinder.find(frequencyBins) ?: return null

        return if (magnitude < 1F) {
            magnitude
        } else {
            1F
        }
    }

}

