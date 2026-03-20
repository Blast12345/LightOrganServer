package dsp.filtering

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class BiquadraticFilter(
    override val sampleRate: Float,
    val b0: Double,
    val b1: Double,
    val b2: Double,
    val a1: Double,
    val a2: Double,
) : SampleFilter {

    private var z1 = 0.0
    private var z2 = 0.0

    override fun filter(samples: FloatArray): FloatArray {
        return FloatArray(samples.size) { process(samples[it]) }
    }

    private fun process(sample: Float): Float {
        val x = sample.toDouble()
        val y = b0 * x + z1

        z1 = b1 * x - a1 * y + z2
        z2 = b2 * x - a2 * y

        return y.toFloat()
    }

    override fun reset() {
        z1 = 0.0
        z2 = 0.0
    }

    companion object {

        fun lowPass(frequency: Double, sampleRate: Double, q: Double): BiquadraticFilter {
            val w0 = 2.0 * PI * frequency / sampleRate
            val cosW0 = cos(w0)
            val alpha = sin(w0) / (2.0 * q)
            val a0 = 1.0 + alpha

            return BiquadraticFilter(
                sampleRate = sampleRate.toFloat(),
                b0 = ((1.0 - cosW0) / 2.0) / a0,
                b1 = (1.0 - cosW0) / a0,
                b2 = ((1.0 - cosW0) / 2.0) / a0,
                a1 = (-2.0 * cosW0) / a0,
                a2 = (1.0 - alpha) / a0,
            )
        }

        fun highPass(frequency: Double, sampleRate: Double, q: Double): BiquadraticFilter {
            val w0 = 2.0 * PI * frequency / sampleRate
            val cosW0 = cos(w0)
            val alpha = sin(w0) / (2.0 * q)
            val a0 = 1.0 + alpha

            return BiquadraticFilter(
                sampleRate = sampleRate.toFloat(),
                b0 = ((1.0 + cosW0) / 2.0) / a0,
                b1 = (-(1.0 + cosW0)) / a0,
                b2 = ((1.0 + cosW0) / 2.0) / a0,
                a1 = (-2.0 * cosW0) / a0,
                a2 = (1.0 - alpha) / a0,
            )
        }

    }

}