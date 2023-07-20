package sound.frequencyBins.filters

import config.Config
import config.ConfigSingleton
import sound.frequencyBins.FrequencyBin

class FrequencyBinDenoiser(
    private val config: Config = ConfigSingleton
) {

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

    private val noiseFloor: Float
        get() = config.noiseFloor

}