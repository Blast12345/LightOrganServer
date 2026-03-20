package dsp.fft

import audio.samples.AudioFormat
import bins.FrequencyBin
import bins.FrequencyBins

class FftFrequencyBinsCalculator(
    private val fftCalculator: FftCalculator = FftCalculator()
) {

    fun calculate(
        frame: FloatArray,
        format: AudioFormat,
        magnitudeCorrectionFactor: Float
    ): FrequencyBins {
        val magnitudes = fftCalculator.calculateMagnitudes(frame)
        val binSpacing = format.nyquistFrequency / magnitudes.size

        return magnitudes.indices.map { index ->
            val rawMagnitude = magnitudes[index]

            FrequencyBin(
                frequency = index * binSpacing,
                magnitude = rawMagnitude * magnitudeCorrectionFactor,
            )
        }
    }

}