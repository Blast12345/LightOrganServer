package dsp.filtering

import kotlin.math.PI
import kotlin.math.tan

class OnePoleOneZeroFilter(
    override val sampleRate: Float,
    val b0: Double,
    val b1: Double,
    val a1: Double,
) : SampleFilter {

    private var z1 = 0.0

    override fun filter(samples: FloatArray): FloatArray {
        return FloatArray(samples.size) { process(samples[it]) }
    }

    private fun process(sample: Float): Float {
        val x = sample.toDouble()
        val y = b0 * x + z1
        z1 = b1 * x - a1 * y
        return y.toFloat()
    }

    override fun reset() {
        z1 = 0.0
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