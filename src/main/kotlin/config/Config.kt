package config

import config.children.Client
import dsp.filtering.config.FilterConfig
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("LongParameterList")
class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val highPassFilter: FilterConfig,
    val lowPassFilter: FilterConfig,
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val magnitudeMultiplier: Float,
)



