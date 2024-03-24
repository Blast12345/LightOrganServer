package toolkit.generators

import kotlin.math.sin

// Reference: https://github.com/wendykierp/JTransforms/issues/4#issuecomment-199352683
class SineWaveGenerator(private val sampleRate: Float) {

    fun generate(frequencies: List<Float>, sampleSize: Int): DoubleArray {
        val signal = DoubleArray(sampleSize)

        for (i in signal.indices) {
            val currentTime = i * (1 / sampleRate)
            var totalAmplitude = 0.0
            for (frequency in frequencies) {
                val amplitude = sin(2 * Math.PI * frequency * currentTime)
                totalAmplitude += amplitude
            }
            signal[i] = totalAmplitude
        }

        return signal
    }

}
