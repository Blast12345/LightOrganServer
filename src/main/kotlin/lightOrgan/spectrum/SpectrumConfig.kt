package lightOrgan.spectrum

import dsp.filtering.config.FilterConfig

data class SpectrumConfig(
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val highPassFilter: FilterConfig?,
    val lowPassFilter: FilterConfig?,
)