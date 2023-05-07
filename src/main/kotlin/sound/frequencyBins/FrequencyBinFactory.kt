package sound.frequencyBins

import config.Config
import config.ConfigProvider

class FrequencyBinFactory(
    private val config: Config = ConfigProvider().current
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