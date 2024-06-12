package config

import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import sound.bins.frequency.filters.Crossover

@Suppress("LongParameterList")
class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val hueSampleSize: Int,
    val hueLowCrossover: Crossover,
    val hueHighCrossover: Crossover,
    val brightnessSampleSize: Int,
    val brightnessLowCrossover: Crossover,
    val brightnessHighCrossover: Crossover,
    val interpolatedSampleSize: Int,
    val brightnessMultiplier: Float,
    val millisecondsToWaitBetweenCheckingForNewAudio: Long
)



