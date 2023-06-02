package sound.frequencyBins

import ConfigSingleton
import config.Config

class FrequencyBinFactory(
    private val config: Config = ConfigSingleton
) {

    private val magnitudeMultiplier: Float
        get() = config.magnitudeMultiplier

    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat() * magnitudeMultiplier
        )
    }

}