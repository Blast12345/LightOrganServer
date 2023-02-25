package sound.frequencyBins

import config.Config

interface FrequencyBinFactoryInterface {
    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin
}

class FrequencyBinFactory(
    private val config: Config = Config()
) : FrequencyBinFactoryInterface {

    private val magnitudeMultiplier = config.magnitudeMultiplier

    override fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat() * magnitudeMultiplier
        )
    }

}