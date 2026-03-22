package dsp.fft

import audio.samples.AudioFrame
import bins.FrequencyBin
import bins.FrequencyBins

class FftFrequencyBinsCalculator(
    private val fftCalculator: FftCalculator = FftCalculator()
) {

    fun calculate(
        frame: AudioFrame,
        magnitudeCorrectionFactor: Float
    ): FrequencyBins {
        val magnitudes = fftCalculator.calculateMagnitudes(frame.samples)
        val binSpacing = frame.format.nyquistFrequency / magnitudes.size

        return magnitudes.indices.map { index ->
            val rawMagnitude = magnitudes[index]

            FrequencyBin(
                frequency = index * binSpacing,
                magnitude = rawMagnitude * magnitudeCorrectionFactor,
            )
        }
    }

}