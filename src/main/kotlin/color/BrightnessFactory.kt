package color

import sound.frequencyBins.FrequencyBin

class BrightnessFactory {

    // TODO: Improve logic around really low values. There is a lot of flickering.
    fun create(frequency: FrequencyBin): Float {
        return if (frequency.magnitude < 1) {
            frequency.magnitude
        } else {
            1F
        }
    }

}