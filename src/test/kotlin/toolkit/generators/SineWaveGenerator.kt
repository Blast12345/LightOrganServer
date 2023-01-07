package toolkit.generators

import kotlin.math.sin

// Reference: https://github.com/wendykierp/JTransforms/issues/4#issuecomment-199352683
class SineWaveGenerator(private val sampleRate: Double) {

    fun generate(frequency: Int = 0, sampleSize: Int): DoubleArray {
        val signal = DoubleArray(sampleSize)

        for (i in signal.indices) {
            val currentTime = i * (1 / sampleRate)
            val amplitude = sin(2 * Math.PI * frequency * currentTime)
            signal[i] = amplitude
        }

        return signal
    }

}