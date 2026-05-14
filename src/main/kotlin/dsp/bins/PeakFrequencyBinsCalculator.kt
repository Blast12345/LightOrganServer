package dsp.bins

import kotlin.math.PI
import kotlin.math.log10
import kotlin.math.pow

class PeakFrequencyBinsCalculator {

    fun calculate(frequencyBins: FrequencyBins): FrequencyBins {
        return frequencyBins
            .sortedBy { it.frequency }
            .windowed(size = 3, step = 1)
            .mapNotNull { (previous, current, next) ->
                if (containsParabolicPeak(previous, current, next)) {
                    interpolate(previous, current, next)
                } else {
                    null
                }
            }
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

        val interpolatedFrequency = current.frequency + delta * binWidth
        val interpolatedMagnitude = 10f.pow(beta - 0.25f * (alpha - gamma) * delta)
        val interpolatedPhase = interpolatePhase(previous, current, next, delta)

        return FrequencyBin(
            frequency = interpolatedFrequency,
            magnitude = interpolatedMagnitude.toDouble(),
            phase = interpolatedPhase
        )
    }

    private fun interpolatePhase(
        previous: FrequencyBin,
        current: FrequencyBin,
        next: FrequencyBin,
        delta: Float
    ): Double {
        val phaseCenter = current.value.argument
        val phasePrevious = phaseCenter + wrapToPi(previous.value.argument - phaseCenter)
        val phaseNext = phaseCenter + wrapToPi(next.value.argument - phaseCenter)

        return phaseCenter + delta * (phaseNext - phasePrevious) / 2.0
    }

    private fun wrapToPi(angle: Double): Double {
        var wrapped = angle.rem(2.0 * PI)
        if (wrapped > PI) wrapped -= 2.0 * PI
        if (wrapped < -PI) wrapped += 2.0 * PI
        return wrapped
    }

}