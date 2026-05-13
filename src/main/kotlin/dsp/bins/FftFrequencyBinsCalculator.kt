package dsp.bins

import org.apache.commons.math3.complex.Complex
import org.jtransforms.fft.FloatFFT_1D

class FftFrequencyBinsCalculator : FrequencyBinsCalculator {

    override fun calculate(
        monoSamples: FloatArray,
        sampleRate: Float,
        magnitudeCorrectionFactor: Float
    ): FrequencyBins {
        val complexValues = extractComplexValues(monoSamples)
        val binSpacing = sampleRate / monoSamples.size
        val fftScalingFactor = 2.0 / monoSamples.size

        return complexValues.mapIndexed { index, value ->
            FrequencyBin(
                frequency = index * binSpacing,
                value = value.multiply(fftScalingFactor * magnitudeCorrectionFactor),
            )
        }
    }

    private fun extractComplexValues(samples: FloatArray): List<Complex> {
        val fftOutput = computeFft(samples)
        val binCount = samples.size / 2

        return (0..binCount).map { index ->
            when (index) {
                0 -> Complex(fftOutput[0].toDouble(), 0.0)
                binCount -> Complex(fftOutput[1].toDouble(), 0.0)
                else -> Complex(
                    fftOutput[2 * index].toDouble(),
                    fftOutput[2 * index + 1].toDouble()
                )
            }
        }
    }

    private fun computeFft(frame: FloatArray): FloatArray {
        val output = frame.copyOf()
        FloatFFT_1D(output.size.toLong()).realForward(output)
        return output
    }

}