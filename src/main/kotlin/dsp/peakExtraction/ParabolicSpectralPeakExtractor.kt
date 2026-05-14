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
        return current.magnitude >= previous.magnitude
                && current.magnitude >= next.magnitude
                && (current.magnitude > previous.magnitude || current.magnitude > next.magnitude)
    }

    private fun interpolate(previous: FrequencyBin, current: FrequencyBin, next: FrequencyBin): SpectralPeak {
        val previousLogMagnitude = log10(previous.magnitude.coerceAtLeast(Float.MIN_VALUE))
        val currentLogMagnitude = log10(current.magnitude.coerceAtLeast(Float.MIN_VALUE))
        val nextLogMagnitude = log10(next.magnitude.coerceAtLeast(Float.MIN_VALUE))

        val denominator = previousLogMagnitude - 2 * currentLogMagnitude + nextLogMagnitude
        if (denominator == 0F) return SpectralPeak(current.frequency, current.magnitude)

        val delta = 0.5F * (previousLogMagnitude - nextLogMagnitude) / denominator
        val binWidth = next.frequency - current.frequency

        return SpectralPeak(
            frequency = current.frequency + delta * binWidth,
            magnitude = 10f.pow(currentLogMagnitude - 0.25f * (previousLogMagnitude - nextLogMagnitude) * delta)
        )
    }

}