package dsp.filtering

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// NOTE: This is a -12 dB/octave filter. Different coefficients change the shape of attenuation, illustrated by factory methods.
// b0 - how much the current input matters
// b1 - how much the input from one step ago matters
// b2 - how much the input from two steps ago matters
// a1 - how much the output from one step ago matters
// a2 - how much the output from two steps ago matters
class BiquadraticFilter(
    override val sampleRate: Float,
    val b0: Double,
    val b1: Double,
    val b2: Double,
    val a1: Double,
    val a2: Double,
) : OrderedFilter {

    override val order: Int = 2
    private var z1 = 0.0
    private var z2 = 0.0

    override fun filter(samples: FloatArray): FloatArray {
        return FloatArray(samples.size) { process(samples[it]) }
    }

    private fun process(sample: Float): Float {
        val x = sample.toDouble() // input
        val y = b0 * x + z1 // output

        z1 = b1 * x - a1 * y + z2
        z2 = b2 * x - a2 * y

        return y.toFloat()
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