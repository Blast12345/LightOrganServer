package lightOrgan.sound.frequencyBins

import config.Config
import config.ConfigSingleton

interface FrequencyBinFactoryInterface {
    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin
}

class FrequencyBinFactory(
    private val config: Config = ConfigSingleton
) : FrequencyBinFactoryInterface {

    private val magnitudeMultiplier = config.magnitudeMultiplier

    override fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat() * magnitudeMultiplier
        )
    }

}