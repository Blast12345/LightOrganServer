package config

import bins.HighPassFilter
import bins.LowPassFilter
import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("LongParameterList")
class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val highPassFilter: HighPassFilter?, // e.g., allow bins over 30 hz
    val lowPassFilter: LowPassFilter?, // e.g., allow bins under 120 hz
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val magnitudeMultiplier: Float,
)



