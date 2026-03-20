package bins

import audio.samples.AudioFrame
import kotlin.math.*

// TODO: Test me
class HighPassFilter(
    val frequency: Float,
    val slope: Float
) {

    fun trim(bins: FrequencyBins, thresholdDb: Float = 48f): FrequencyBins {
        val hardCutoff = frequency / 2f.pow(thresholdDb / slope)
        return bins.filter { it.frequency >= hardCutoff }
    }

    private var stages: List<Biquad> = emptyList()

    private fun buildStages(sampleRate: Float): List<Biquad> {
        val order = (slope / 6f).roundToInt().coerceAtLeast(2).let {
            if (it % 2 != 0) it + 1 else it // Must be even for biquad pairs
        }
        val numStages = order / 2

        return List(numStages) { k ->
            val q = butterworthQ(order, k)
            Biquad.highPass(frequency.toDouble(), sampleRate.toDouble(), q)
        }
    }

    fun filter(audio: AudioFrame): AudioFrame {
        if (stages.isEmpty()) {
            stages = buildStages(audio.format.sampleRate)
        }

        var samples = audio.samples
        for (stage in stages) {
            samples = stage.process(samples)
        }
        return AudioFrame(samples, audio.format)
    }

    fun reset() = stages.forEach { it.reset() }

}

/**
 * Q factor for the k-th biquad stage (0-indexed)
 * of an Nth-order Butterworth filter.
 */
fun butterworthQ(order: Int, stageIndex: Int): Double {
    val pole = PI * (2 * stageIndex + 1) / (2.0 * order)
    return 1.0 / (2.0 * cos(pole))
}

class Biquad(
    private var b0: Double = 0.0,
    private var b1: Double = 0.0,
    private var b2: Double = 0.0,
    private var a1: Double = 0.0,
    private var a2: Double = 0.0,
) {
    // Direct Form II Transposed state
    private var z1 = 0.0
    private var z2 = 0.0

    fun process(sample: Float): Float {
        val x = sample.toDouble()
        val y = b0 * x + z1
        z1 = b1 * x - a1 * y + z2
        z2 = b2 * x - a2 * y
        return y.toFloat()
    }

    fun process(samples: FloatArray): FloatArray {
        return FloatArray(samples.size) { process(samples[it]) }
    }

    fun reset() {
        z1 = 0.0
        z2 = 0.0
    }

    companion object {
        fun highPass(frequency: Double, sampleRate: Double, q: Double): Biquad {
            val w0 = 2.0 * PI * frequency / sampleRate
            val cosW0 = cos(w0)
            val alpha = sin(w0) / (2.0 * q)

            val a0 = 1.0 + alpha
            return Biquad(
                b0 = ((1.0 + cosW0) / 2.0) / a0,
                b1 = (-(1.0 + cosW0)) / a0,
                b2 = ((1.0 + cosW0) / 2.0) / a0,
                a1 = (-2.0 * cosW0) / a0,
                a2 = (1.0 - alpha) / a0,
            )
        }

        fun lowPass(frequency: Double, sampleRate: Double, q: Double): Biquad {
            val w0 = 2.0 * PI * frequency / sampleRate
            val cosW0 = cos(w0)
            val alpha = sin(w0) / (2.0 * q)

            val a0 = 1.0 + alpha
            return Biquad(
                b0 = ((1.0 - cosW0) / 2.0) / a0,
                b1 = (1.0 - cosW0) / a0,
                b2 = ((1.0 - cosW0) / 2.0) / a0,
                a1 = (-2.0 * cosW0) / a0,
                a2 = (1.0 - alpha) / a0,
            )
        }
    }
}