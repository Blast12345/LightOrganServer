package config

import config.children.Client
import sound.bins.frequency.filters.Crossover
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("LongParameterList")
class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val lowCrossover: sound.bins.frequency.filters.Crossover,
    val highCrossover: sound.bins.frequency.filters.Crossover,
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val spectrumMultiplier: Float,
)



