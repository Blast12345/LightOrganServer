package sound.frequencyBins

import config.Config

interface FrequencyBinFactoryInterface {
    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin
}

class FrequencyBinFactory(
    config: Config
) : FrequencyBinFactoryInterface {
    
    private val magnitudeMultiplier = config.magnitudeMultiplier

    override fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity * 2, // TODO: Account for number of channels
            magnitude = magnitude.toFloat() * magnitudeMultiplier
        )
    }

}