package dsp.peakExtraction

import kotlin.math.pow
import kotlin.math.sqrt

data class SpectralPeak(
    val frequency: Float,
    val magnitude: Float
)

typealias SpectralPeaks = List<SpectralPeak>

/**
 * Since peaks are at distinct frequencies, their energies sum directly (Parseval's theorem).
 */
val SpectralPeaks.combinedMagnitude: Double
    get() {
        val sumOfSquares = fold(0.0) { sum, peak -> sum + peak.magnitude.toDouble().pow(2) }
        return sqrt(sumOfSquares)
    }