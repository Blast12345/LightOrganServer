package colorListener.sound.fft

import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.sqrt

interface FftAlgorithmInterface {
    fun process(signal: DoubleArray): DoubleArray
}

class FftAlgorithm : FftAlgorithmInterface {

    // Reference: the "Extract Real" section from https://github.com/wendykierp/JTransforms/issues/4#issue-142313147
    override fun process(signal: DoubleArray): DoubleArray {
        val doubleFFT = DoubleFFT_1D(signal.size.toLong())
        doubleFFT.realForward(signal)
        return createAmplitudes(signal)
    }

    private fun createAmplitudes(signal: DoubleArray): DoubleArray {
        val amplitudes = DoubleArray(signal.size / 2)

        for (i in amplitudes.indices) {
            val real = signal[i * 2]
            val imaginary = signal[i * 2 + 1]
            amplitudes[i] = sqrt(real * real + imaginary * imaginary) / signal.size
        }

        return amplitudes
    }
}