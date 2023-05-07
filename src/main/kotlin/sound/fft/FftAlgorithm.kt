package sound.fft

import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.sqrt

// Reference: https://github.com/wendykierp/JTransforms/issues/4#issue-142313147
class FftAlgorithm {

    fun calculateRelativeMagnitudes(samples: DoubleArray): DoubleArray {
        val fftData = performFFT(samples)
        return getRelativeMagnitudes(fftData)
    }

    private fun performFFT(samples: DoubleArray): DoubleArray {
        val output = samples.copyOf()
        val doubleFFT = DoubleFFT_1D(output.size.toLong())
        doubleFFT.realForward(output)
        return output
    }

    private fun getRelativeMagnitudes(samples: DoubleArray): DoubleArray {
        // NOTE: The output is half the size of the input because it requires
        // two indices (a real number and imaginary number) to calculate the magnitude
        val magnitudes = DoubleArray(samples.size / 2)

        for (i in magnitudes.indices) {
            val real = samples[i * 2]
            val imaginary = samples[i * 2 + 1]
            magnitudes[i] = calculateMagnitude(real, imaginary)
        }

        return magnitudes
    }

    private fun calculateMagnitude(real: Double, imaginary: Double): Double {
        // NOTE: My magnitudes are consistently half of what I would expect.
        // I'm guessing this is because FFT results are mirrored, but I'm only using one side.
        // Because I'm ignoring the other side, my energy is halved.
        // I must multiply by two to compensate.
        return sqrt(real * real + imaginary * imaginary) * 2
    }

}