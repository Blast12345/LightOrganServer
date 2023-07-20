package sound.frequencyBins

import config.Config
import config.ConfigSingleton

class FrequencyBinFactory(
    private val config: Config = ConfigSingleton
) {

    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat() * magnitudeMultiplier
        )
    }

    private val magnitudeMultiplier: Float
        get() = config.magnitudeMultiplier

}