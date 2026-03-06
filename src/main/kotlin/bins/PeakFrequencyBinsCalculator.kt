package bins

import kotlin.math.log10
import kotlin.math.pow

// NOTE: This is using parabolic interpolation. If more strategies are added, then create an appropriate interface and give this class a more specific name.
class PeakFrequencyBinsCalculator {

    fun calculate(frequencyBins: FrequencyBins): FrequencyBins {
        val sortedBins = frequencyBins.sortedBy { it.frequency }
        val peaks: MutableList<FrequencyBin> = mutableListOf()

        for (i in sortedBins.indices) {
            val previous = sortedBins.elementAtOrNull(i - 1) ?: continue
            val current = sortedBins.elementAtOrNull(i) ?: continue
            val next = sortedBins.elementAtOrNull(i + 1) ?: continue

            if (!containsParabolicPeak(previous, current, next)) continue

            val interpolatedBin = interpolate(previous, current, next)
            peaks.add(interpolatedBin)
        }

        return peaks
    }

    private fun containsParabolicPeak(previous: FrequencyBin, current: FrequencyBin, next: FrequencyBin): Boolean {
        return current.magnitude >= previous.magnitude
                && current.magnitude >= next.magnitude
                && (current.magnitude > previous.magnitude || current.magnitude > next.magnitude)
    }

    private fun interpolate(previous: FrequencyBin, current: FrequencyBin, next: FrequencyBin): FrequencyBin {
        val alpha = log10(previous.magnitude.coerceAtLeast(Float.MIN_VALUE))
        val beta = log10(current.magnitude.coerceAtLeast(Float.MIN_VALUE))
        val gamma = log10(next.magnitude.coerceAtLeast(Float.MIN_VALUE))

        val denominator = alpha - 2 * beta + gamma
        if (denominator == 0F) return current

        val delta = 0.5F * (alpha - gamma) / denominator
        val binWidth = next.frequency - current.frequency
        val logMagnitude = beta - 0.25F * (alpha - gamma) * delta

        return FrequencyBin(
            frequency = current.frequency + delta * binWidth,
            magnitude = 10F.pow(logMagnitude)
        )
    }

}