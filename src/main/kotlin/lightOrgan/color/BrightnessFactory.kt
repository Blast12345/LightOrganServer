package lightOrgan.color

import lightOrgan.sound.frequencyBins.FrequencyBin

interface BrightnessFactoryInterface {
    fun create(frequency: FrequencyBin): Float
}

class BrightnessFactory : BrightnessFactoryInterface {

    // TODO: Improve logic around really low values. There is a lot of flickering.
    override fun create(frequency: FrequencyBin): Float {
        return if (frequency.magnitude < 1) {
            frequency.magnitude
        } else {
            1F
        }
    }

}