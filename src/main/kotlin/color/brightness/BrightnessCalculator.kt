package color.brightness

import sound.frequencyBins.FrequencyBinList

class BrightnessCalculator(
    private val greatestMagnitudeCalculator: GreatestMagnitudeCalculator = GreatestMagnitudeCalculator(),
) {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        val magnitude = greatestMagnitudeCalculator.calculate(frequencyBins) ?: return null

        return if (magnitude < 1F) {
            magnitude
        } else {
            1F
        }
    }

}

