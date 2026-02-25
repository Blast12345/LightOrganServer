package dsp.bins

import audio.samples.AudioFormat
import dsp.bins.frequency.FrequencyBin
import dsp.bins.frequency.FrequencyBins
import dsp.fft.FftCalculator

class FrequencyBinsCalculator(
    private val fftCalculator: FftCalculator = FftCalculator()
) {

    fun calculate(frame: FloatArray, format: AudioFormat): FrequencyBins {
        val magnitudes = fftCalculator.calculateMagnitudes(frame)
        val binSpacing = format.nyquistFrequency / magnitudes.size

        val allBins = magnitudes.indices.map { index -> FrequencyBin(index * binSpacing, magnitudes[index]) }
        return allBins.dropDcOffset()
    }

    private fun FrequencyBins.dropDcOffset(): FrequencyBins {
        // The first bin (e.g., 0 Hz) is always the DC offset, not a real frequency bin.
        return drop(1)
    }

}
