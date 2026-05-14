package dsp.peakExtraction

data class SpectralPeak(
    val frequency: Float,
    val magnitude: Float
)

typealias SpectralPeaks = List<SpectralPeak>