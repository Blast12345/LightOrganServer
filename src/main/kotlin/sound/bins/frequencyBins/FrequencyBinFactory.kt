package sound.bins.frequencyBins

import config.ConfigSingleton

class FrequencyBinFactory(
    private val magnitudeMultiplier: Float = ConfigSingleton.magnitudeMultiplier
) {

    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat() * magnitudeMultiplier
        )
    }

}