package dsp.peakExtraction

import dsp.bins.FrequencyBin
import dsp.bins.FrequencyBins
import kotlin.math.log10
import kotlin.math.pow

class ParabolicSpectralPeakExtractor : SpectralPeakExtractor {

    override fun extract(spectrum: FrequencyBins): SpectralPeaks {
        return spectrum
            .windowed(size = 3, step = 1)
            .mapNotNull { (previous, current, next) ->
                if (containsPeak(previous, current, next)) {
                    interpolate(previous, current, next)
                } else {
                    null
                }
            }
    }

    private fun containsPeak(previous: FrequencyBin, current: FrequencyBin, next: FrequencyBin): Boolean {
        // Peak detection is biased to one side to avoid duplicates when plateaus are found
        // e.g. magnitudes [0.1, 0.5, 0.5, 0.1] should result in one peak between the 0.5 values
        return current.magnitude > previous.magnitude && current.magnitude >= next.magnitude
    }

    private fun interpolate(previous: FrequencyBin, current: FrequencyBin, next: FrequencyBin): SpectralPeak {
        val previousLog = safeLog10(previous.magnitude)
        val currentLog = safeLog10(current.magnitude)
        val nextLog = safeLog10(next.magnitude)

        val slope = previousLog - nextLog
        val curvature = previousLog - 2 * currentLog + nextLog
        check(curvature < 0) { "Curvature must be negative (concave down); containsPeak should guarantee this" }

        val delta = 0.5f * slope / curvature
        val binWidth = next.frequency - current.frequency
        val interpolatedLogMagnitude = currentLog - 0.25f * slope * delta

        return SpectralPeak(
            frequency = current.frequency + delta * binWidth,
            magnitude = 10f.pow(interpolatedLogMagnitude)
        )
    }

    private fun safeLog10(value: Float): Float = log10(value.coerceAtLeast(Float.MIN_VALUE))

}