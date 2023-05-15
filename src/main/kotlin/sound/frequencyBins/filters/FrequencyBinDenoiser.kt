package sound.frequencyBins.filters

import ConfigSingleton
import config.Config
import sound.frequencyBins.FrequencyBin

class FrequencyBinDenoiser(
    private val config: Config = ConfigSingleton
) {

    private val noiseFloor: Float
        get() = config.noiseFloor

    fun denoise(frequencyBin: FrequencyBin): FrequencyBin {
        return FrequencyBin(
            frequency = frequencyBin.frequency,
            magnitude = getMagnitude(frequencyBin.magnitude)
        )
    }

    private fun getMagnitude(magnitude: Float): Float {
        return if (magnitude < noiseFloor) {
            0F
        } else {
            magnitude
        }
    }

}