package color

import sound.frequencyBins.FrequencyBin

interface BrightnessFactoryInterface {
    fun create(frequency: FrequencyBin): Float
}

class BrightnessFactory : BrightnessFactoryInterface {

    override fun create(frequency: FrequencyBin): Float {
        return if (frequency.magnitude < 1) {
            frequency.magnitude
        } else {
            1F
        }
    }

}