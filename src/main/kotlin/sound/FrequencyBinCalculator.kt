package sound

import audio.samples.AudioFormat
import audio.samples.AudioFrame
import sound.bins.frequency.FrequencyBinFactory
import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.listCalculator.GranularityCalculator
import sound.bins.frequency.listCalculator.MagnitudeListCalculator
import sound.signalProcessor.SignalProcessor

class FrequencyBinsCalculator(
    private val signalProcessor: SignalProcessor = SignalProcessor(),
    private val magnitudeListCalculator: MagnitudeListCalculator = MagnitudeListCalculator(), // TODO: Rename
    private val granularityCalculator: GranularityCalculator = GranularityCalculator(),
    private val frequencyBinFactory: FrequencyBinFactory = FrequencyBinFactory(),
) {

    fun calculate(audioFrame: AudioFrame): FrequencyBins {
        val doubleArraySamples = audioFrame.samples.map { it.toDouble() }.toDoubleArray()
        val processedSamples = signalProcessor.process(doubleArraySamples) // TODO: Change array type
        val magnitudes = magnitudeListCalculator.calculate(processedSamples)
        // TODO: Drop the second half of magnitudes, thus skipping the need for Nyquist filtering.
        val granularity = granularityCalculator.calculate(magnitudes.size, audioFrame.format)

        return magnitudes.mapIndexed { index, magnitude ->
            // TODO: Maybe move the frequency calculation into this class, eliminating the need for a factory
            frequencyBinFactory.create(index, granularity, magnitude)
        }.removeBinsBeyondNyquistFrequency(audioFrame.format)
    }

    private fun FrequencyBins.removeBinsBeyondNyquistFrequency(audioFormat: AudioFormat): FrequencyBins {
        return this.filter { it.frequency <= audioFormat.nyquistFrequency }
    }

}
