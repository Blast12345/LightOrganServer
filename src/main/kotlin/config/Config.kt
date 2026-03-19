package config

import config.children.Client
import dsp.filtering.config.FilterConfig
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("LongParameterList")
class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val spectrum: SpectrumConfig,
    val spectrumGui: SpectrumGuiConfig,
    val brightnessMultiplier: Float,
)

data class SpectrumConfig(
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val highPassFilter: FilterConfig?,
    val lowPassFilter: FilterConfig?,
)

data class SpectrumGuiConfig(
    val scale: Float,
    val lowestFrequency: Float,
    val highestFrequency: Float,
)