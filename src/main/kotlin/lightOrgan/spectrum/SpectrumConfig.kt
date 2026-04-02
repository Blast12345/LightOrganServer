package lightOrgan.spectrum

import dsp.filtering.FilterConfig
import dsp.windowing.WindowType
import kotlin.time.Duration

data class SpectrumConfig(
    val frameDuration: Duration,
    val approximateBinSpacing: Float,
    val rolloffThreshold: Float, // e.g. -48 dBFS
    val highPassFilter: FilterConfig?,
    val lowPassFilter: FilterConfig?,
    val window: WindowType
)
