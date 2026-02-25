package config

import config.children.Client
import dsp.bins.frequency.filters.Crossover
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("LongParameterList")
class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val lowCrossover: Crossover,
    val highCrossover: Crossover,
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val spectrumMultiplier: Float,
)



