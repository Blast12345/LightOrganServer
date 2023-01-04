package sound.fft

import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.sqrt

interface FftAlgorithmInterface {
    fun calculateRelativeAmplitudes(signal: DoubleArray): DoubleArray
}

// Reference: https://github.com/wendykierp/JTransforms/issues/4#issue-142313147
class FftAlgorithm : FftAlgorithmInterface {

    override fun calculateRelativeAmplitudes(signal: DoubleArray): DoubleArray {
        val doubleFFT = DoubleFFT_1D(signal.size.toLong())
        doubleFFT.realForward(signal)
        return getAmplitudes(signal)
    }

    private fun getAmplitudes(signal: DoubleArray): DoubleArray {
        // NOTE: I'm not entirely sure why we divide by two.
        // Maybe this is due to the Nyquist Frequency?
        val amplitudes = DoubleArray(signal.size / 2)

        for (i in amplitudes.indices) {
            val real = signal[i * 2]
            val imaginary = signal[i * 2 + 1]
            amplitudes[i] = calculateAmplitude(real, imaginary, signal.size)
        }

        return amplitudes
    }

    private fun calculateAmplitude(real: Double, imaginary: Double, sampleSize: Int): Double {
        return sqrt(real * real + imaginary * imaginary) / sampleSize
    }
}