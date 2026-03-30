package lightOrgan.spectrum

import dsp.filtering.config.FilterConfig
import kotlin.time.Duration

data class SpectrumConfig(
    val frameDuration: Duration,
    val approximateBinSpacing: Float,
    val highPassFilter: FilterConfig.HighPass?,
    val lowPassFilter: FilterConfig.LowPass?,
)

