package sound.frequencyBins

import config.Config
import config.ConfigProvider

interface FrequencyBinFactoryInterface {
    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin
}

class FrequencyBinFactory(
    private val config: Config = ConfigProvider().current
) : FrequencyBinFactoryInterface {

    private val magnitudeMultiplier: Float
        get() = config.magnitudeMultiplier

    override fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat() * magnitudeMultiplier
        )
    }

}