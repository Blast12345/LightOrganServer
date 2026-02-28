package sound.bins.frequency.dominant.frequency

import dsp.fft.FrequencyBin
import dsp.fft.FrequencyBins
import kotlin.math.log10
import kotlin.math.pow

// TODO: More like a caculator with the parabolic function
class PeakFrequencyBinsFinder {

    fun find(frequencyBins: FrequencyBins): FrequencyBins {
        val peaks: MutableList<FrequencyBin> = mutableListOf()

        for (i in frequencyBins.indices) {
            val previousBin = frequencyBins.elementAtOrNull(i - 1)
            val currentBin = frequencyBins.elementAtOrNull(i)
            val nextBin = frequencyBins.elementAtOrNull(i + 1)

            val previousMagnitude = previousBin?.magnitude ?: 0F
            val currentBinMagnitude = currentBin?.magnitude ?: 0F
            val nextBinMagnitude = nextBin?.magnitude ?: 0F

            val isGreaterThanOrEqualToPrevious = currentBinMagnitude >= previousMagnitude
            val isGreaterThanOrEqualToNext = currentBinMagnitude >= nextBinMagnitude
            val isPeak = isGreaterThanOrEqualToPrevious && isGreaterThanOrEqualToNext

            if (isPeak && currentBin != null) {
                peaks.add(currentBin)
            }
        }

        return peaks
    }

}

// PARABOLIC
class PeakFrequencyBinsCalculator {

    fun calculate(frequencyBins: FrequencyBins): FrequencyBins {
        val peaks: MutableList<FrequencyBin> = mutableListOf()

        for (i in frequencyBins.indices) {
            val previous = frequencyBins.elementAtOrNull(i - 1) ?: continue
            val current = frequencyBins.elementAtOrNull(i) ?: continue
            val next = frequencyBins.elementAtOrNull(i + 1) ?: continue

            val alpha = 20 * log10(previous.magnitude.coerceAtLeast(Float.MIN_VALUE))
            val beta = 20 * log10(current.magnitude.coerceAtLeast(Float.MIN_VALUE))
            val gamma = 20 * log10(next.magnitude.coerceAtLeast(Float.MIN_VALUE))

            val isPeak = beta >= alpha && beta >= gamma

            if (!isPeak) continue

            val denominator = alpha - 2 * beta + gamma
            if (denominator == 0F) {
                peaks.add(current)
                continue
            }

            val delta = 0.5F * (alpha - gamma) / denominator
            val interpolatedMagnitudeDb = beta - 0.25F * (alpha - gamma) * delta
            val interpolatedMagnitude = 10F.pow(interpolatedMagnitudeDb / 20F)

            val binWidth = next.frequency - current.frequency
            val interpolatedFrequency = current.frequency + delta * binWidth

            peaks.add(FrequencyBin(interpolatedFrequency, interpolatedMagnitude))
        }

        return peaks
    }

}

// Centroid
//class PeakFrequencyBinsCalculator(
//    private val windowSize: Int = 2
//) {
//
//    fun calculate(frequencyBins: FrequencyBins): FrequencyBins {
//        val peaks: MutableList<FrequencyBin> = mutableListOf()
//
//        for (i in frequencyBins.indices) {
//            val previous = frequencyBins.elementAtOrNull(i - 1) ?: continue
//            val current = frequencyBins.elementAtOrNull(i) ?: continue
//            val next = frequencyBins.elementAtOrNull(i + 1) ?: continue
//
//            if (current.magnitude < previous.magnitude || current.magnitude < next.magnitude) continue
//
//            val start = (i - windowSize).coerceAtLeast(0)
//            val end = (i + windowSize).coerceAtMost(frequencyBins.lastIndex)
//
//            var weightedSum = 0F
//            var magnitudeSum = 0F
//            for (j in start..end) {
//                weightedSum += frequencyBins[j].frequency * frequencyBins[j].magnitude
//                magnitudeSum += frequencyBins[j].magnitude
//            }
//
//            if (magnitudeSum == 0F) continue
//
//            val interpolatedFrequency = weightedSum / magnitudeSum
//            peaks.add(FrequencyBin(interpolatedFrequency, current.magnitude))
//        }
//
//        return peaks
//    }
//
//}