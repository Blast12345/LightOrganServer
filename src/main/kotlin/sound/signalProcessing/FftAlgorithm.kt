package sound.signalProcessing

import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.sqrt

interface FftAlgorithmInterface {
    fun calculateMagnitudes(samples: DoubleArray): DoubleArray
}

// Reference: https://github.com/wendykierp/JTransforms/issues/4#issue-142313147
class FftAlgorithm : FftAlgorithmInterface {

    override fun calculateMagnitudes(samples: DoubleArray): DoubleArray {
        val fftData = performFFT(samples)
        return getMagnitudes(fftData)
    }

    private fun performFFT(samples: DoubleArray): DoubleArray {
        val output = samples.copyOf()
        val doubleFFT = DoubleFFT_1D(output.size.toLong())
        doubleFFT.realForward(output)
        return output
    }

    private fun getMagnitudes(samples: DoubleArray): DoubleArray {
        // NOTE: I'm not entirely sure why we divide by two.
        // Maybe this is due to the Nyquist Frequency?
        val magnitudes = DoubleArray(samples.size / 2)

        for (i in magnitudes.indices) {
            val real = samples[i * 2]
            val imaginary = samples[i * 2 + 1]
            magnitudes[i] = calculateMagnitude(real, imaginary)
        }

        return magnitudes
    }

    private fun calculateMagnitude(real: Double, imaginary: Double): Double {
        return sqrt(real * real + imaginary * imaginary)
    }

}