package color

import sound.frequencyBins.FrequencyBin

interface BrightnessFactoryInterface {
    fun create(frequency: FrequencyBin): Float
}

class BrightnessFactory : BrightnessFactoryInterface {

    override fun create(frequency: FrequencyBin): Float {
        return frequency.magnitude
    }

}