package lightOrgan.spectrum

import dsp.filtering.config.FilterConfig
import dsp.windowing.WindowType
import kotlin.time.Duration

data class SpectrumConfig(
    val frameDuration: Duration,
    val approximateBinSpacing: Float,
    val highPassFilter: FilterConfig?,
    val lowPassFilter: FilterConfig?,
    val window: WindowType
)