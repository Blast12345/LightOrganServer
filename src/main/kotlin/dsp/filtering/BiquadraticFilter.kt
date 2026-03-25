package dsp.filtering

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
) : Filter {

    override val order = 2
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

    // TODO: Test me
    override fun magnitudeAt(frequency: Float): Float {
        val w = 2.0 * PI * frequency / sampleRate

        val cosW = cos(w)
        val cos2W = cos(2 * w)
        val sinW = sin(w)
        val sin2W = sin(2 * w)

        val numReal = b0 + b1 * cosW + b2 * cos2W
        val numImag = -(b1 * sinW + b2 * sin2W)

        val denReal = 1.0 + a1 * cosW + a2 * cos2W
        val denImag = -(a1 * sinW + a2 * sin2W)

        val numMag = sqrt(numReal * numReal + numImag * numImag)
        val denMag = sqrt(denReal * denReal + denImag * denImag)

        return (numMag / denMag).toFloat()
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