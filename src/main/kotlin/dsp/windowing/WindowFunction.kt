package dsp.windowing

import kotlin.math.sqrt

interface WindowFunction {

    // coefficients are the scale for each given position in the window, producing the window shape
    fun coefficients(size: Int): FloatArray

    fun magnitudeCorrectionFactor(sampleSize: Int): Float {
        val coefficients = coefficients(sampleSize)
        return coefficients.size / coefficients.sum()
    }

    fun energyCorrectionFactor(sampleSize: Int): Float {
        val coefficients = coefficients(sampleSize)
        return sqrt(coefficients.size / coefficients.map { coefficient -> coefficient * coefficient }.sum())
    }

    fun appliedTo(frame: FloatArray): FloatArray {
        val coefficients = coefficients(frame.size)
        return FloatArray(frame.size) { index -> coefficients[index] * frame[index] }
    }

}