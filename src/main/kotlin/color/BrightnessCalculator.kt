package color

import dsp.bins.FrequencyBins
import sound.bins.frequency.GreatestMagnitudeFinder

class BrightnessCalculator(
    private val greatestMagnitudeFinder: GreatestMagnitudeFinder = GreatestMagnitudeFinder(),
) {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        // TODO: How to scale?
        val magnitude = greatestMagnitudeFinder.find(frequencyBins) ?: return null

        return if (magnitude < 1F) {
            magnitude.coerceIn(0F, 1F)
        } else {
            1F
        }
    }

}

