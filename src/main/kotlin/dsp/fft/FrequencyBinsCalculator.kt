package dsp.fft

import audio.samples.AudioFormat

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