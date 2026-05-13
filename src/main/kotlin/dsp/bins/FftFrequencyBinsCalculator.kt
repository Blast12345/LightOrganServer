package dsp.bins

import org.apache.commons.math3.complex.Complex
import org.jtransforms.fft.FloatFFT_1D

class FftFrequencyBinsCalculator : FrequencyBinsCalculator {

    override fun calculate(
        monoSamples: FloatArray,
        sampleRate: Float,
        magnitudeCorrectionFactor: Float // TODO: Double?
    ): FrequencyBins {
        val fftData = computeFft(monoSamples)
        val binCount = fftData.size / 2
        val nyquistFrequency = sampleRate / 2f
        val binSpacing = nyquistFrequency / binCount
        val singleSidedCompensation = 2.0
        val fftAmplitudeScaling = 1.0 / monoSamples.size

        val scaling = singleSidedCompensation * fftAmplitudeScaling * magnitudeCorrectionFactor

        return (0 until binCount).map { index ->
            val real = fftData[index * 2] * scaling
            val imaginary = fftData[index * 2 + 1] * scaling

            FrequencyBin(
                frequency = index * binSpacing,
                value = Complex(real, imaginary)
            )
        }
    }

    private fun computeFft(frame: FloatArray): FloatArray {
        val output = frame.copyOf()
        FloatFFT_1D(output.size.toLong()).realForward(output)
        return output
    }

}