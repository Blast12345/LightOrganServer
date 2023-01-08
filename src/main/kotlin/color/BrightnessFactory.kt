package color

import sound.frequencyBins.FrequencyBin

interface BrightnessFactoryInterface {
    fun create(frequency: FrequencyBin): Float
}

// TODO: Test me
class BrightnessFactory : BrightnessFactoryInterface {

    override fun create(frequency: FrequencyBin): Float {
        return frequency.magnitude
    }

}