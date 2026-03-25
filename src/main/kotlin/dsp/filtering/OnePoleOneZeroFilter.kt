package dsp.filtering

import kotlin.math.*

// NOTE: This is a -6 dB/octave filter. Different coefficients change the shape of attenuation, illustrated by factory methods.
// b0 - how much the current input matters
// b1 - how much the input from one step ago matters
// a1 - how much the output from one step ago matters
class OnePoleOneZeroFilter(
    override val sampleRate: Float,
    val b0: Double,
    val b1: Double,
    val a1: Double,
) : Filter {

    override val order = 1
    private var z1 = 0.0

    override fun filter(samples: FloatArray): FloatArray {
        return FloatArray(samples.size) { process(samples[it]) }
    }

    private fun process(sample: Float): Float {
        val x = sample.toDouble() // input
        val y = b0 * x + z1 // output

        z1 = b1 * x - a1 * y

        return y.toFloat()
    }

    // TODO: Test me
    override fun magnitudeAt(frequency: Float): Float {
        val w = 2.0 * PI * frequency / sampleRate

        val cosW = cos(w)
        val sinW = sin(w)

        val numReal = b0 + b1 * cosW
        val numImag = -(b1 * sinW)

        val denReal = 1.0 + a1 * cosW
        val denImag = -(a1 * sinW)

        val numMag = sqrt(numReal * numReal + numImag * numImag)
        val denMag = sqrt(denReal * denReal + denImag * denImag)

        return (numMag / denMag).toFloat()
    }

    companion object {

        fun lowPass(frequency: Double, sampleRate: Double): OnePoleOneZeroFilter {
            val t = tan(PI * frequency / sampleRate)
            val a0 = 1.0 + t

            return OnePoleOneZeroFilter(
                sampleRate = sampleRate.toFloat(),
                b0 = t / a0,
                b1 = t / a0,
                a1 = (t - 1.0) / a0,
            )
        }

        fun highPass(frequency: Double, sampleRate: Double): OnePoleOneZeroFilter {
            val t = tan(PI * frequency / sampleRate)
            val a0 = 1.0 + t

            return OnePoleOneZeroFilter(
                sampleRate = sampleRate.toFloat(),
                b0 = 1.0 / a0,
                b1 = -1.0 / a0,
                a1 = (t - 1.0) / a0,
            )
        }

    }

}