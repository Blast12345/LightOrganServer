package sound.signalProcessor.fft

import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.sqrt

// Reference: https://github.com/wendykierp/JTransforms/issues/4#issue-142313147
class RelativeMagnitudeListCalculator {

    fun calculate(samples: DoubleArray): DoubleArray {
        return getRelativeMagnitudes(
            fftData = getFftData(samples)
        )
    }

    private fun getFftData(samples: DoubleArray): DoubleArray {
        val output = samples.copyOf()
        val doubleFFT = DoubleFFT_1D(output.size.toLong())
        doubleFFT.realForward(output)
        return output
    }

    private fun getRelativeMagnitudes(fftData: DoubleArray): DoubleArray {
        // NOTE: The output is half the size of the input because it requires
        // two indices (a real number and imaginary number) to calculate the magnitude
        val magnitudes = DoubleArray(fftData.size / 2)

        for (i in magnitudes.indices) {
            val real = fftData[i * 2]
            val imaginary = fftData[i * 2 + 1]
            magnitudes[i] = calculateMagnitude(real, imaginary)
        }

        return magnitudes
    }

    private fun calculateMagnitude(real: Double, imaginary: Double): Double {
        // NOTE: The FFT results are mirrored and I suspect the acoustic energy is split
        // between the two sides. Since I'm only using one side, I need to multiply by two.
        return sqrt(real * real + imaginary * imaginary) * 2
    }

}
