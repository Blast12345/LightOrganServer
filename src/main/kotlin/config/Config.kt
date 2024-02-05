package config

import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import sound.bins.frequency.filters.Crossover

@Suppress("LongParameterList")
class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val lowCrossover: Crossover,
    val highCrossover: Crossover,
    val sampleSize: Int,
    val decimationFactor: Int,
    val overlaps: Int,
    val overlapPercent: Float,
    val interpolatedSampleSize: Int,
    val magnitudeMultiplier: Float,
    val millisecondsToWaitBetweenCheckingForNewAudio: Long
) {

    fun getDecimatedSampleSize(): Int {
        return sampleSize / decimationFactor
    }

}



