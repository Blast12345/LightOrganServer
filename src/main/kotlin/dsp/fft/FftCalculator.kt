package dsp.fft

import org.jtransforms.fft.FloatFFT_1D
import kotlin.math.sqrt

class FftCalculator {

    fun calculateMagnitudes(frame: FloatArray): FloatArray {
        val fftData = computeFft(frame)
        val binCount = fftData.size / 2

        return FloatArray(binCount) { i ->
            val real = fftData[i * 2]
            val imaginary = fftData[i * 2 + 1]
            sqrt(real * real + imaginary * imaginary) * 2 / frame.size
        }
    }

    private fun computeFft(frame: FloatArray): FloatArray {
        val output = frame.copyOf()
        FloatFFT_1D(output.size.toLong()).realForward(output)
        return output
    }

}