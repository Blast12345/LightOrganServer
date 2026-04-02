package lightOrgan.spectrum

import dsp.filtering.FilterConfig
import dsp.windowing.WindowType

data class SpectrumConfig(
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val highPassFilter: FilterConfig?,
    val lowPassFilter: FilterConfig?,
    val window: WindowType
)