package lightOrgan.sound.frequencyBins

import config.PersistedConfig

interface FrequencyBinFactoryInterface {
    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin
}

class FrequencyBinFactory(
    private val magnitudeMultiplier: Float = PersistedConfig().magnitudeMultiplier
) : FrequencyBinFactoryInterface {

    override fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat() * magnitudeMultiplier
        )
    }

}