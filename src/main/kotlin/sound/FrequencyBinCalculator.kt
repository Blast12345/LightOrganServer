package sound

import input.samples.AudioFormat
import input.samples.AudioFrame
import sound.bins.frequency.FrequencyBinFactory
import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.listCalculator.GranularityCalculator
import sound.bins.frequency.listCalculator.MagnitudeListCalculator
import sound.signalProcessor.SignalProcessor

class FrequencyBinsCalculator(
    private val signalProcessor: SignalProcessor = SignalProcessor(),
    private val magnitudeCalculator: MagnitudeListCalculator = MagnitudeListCalculator(), // TODO: Rename
    private val granularityCalculator: GranularityCalculator = GranularityCalculator(),
    private val frequencyBinFactory: FrequencyBinFactory = FrequencyBinFactory()
) {

    fun calculate(audioFrame: AudioFrame): FrequencyBins {
        val doubleArraySamples = audioFrame.samples.map { it.toDouble() }.toDoubleArray()
        val processedSamples = signalProcessor.process(doubleArraySamples) // TODO: Change array type
        val magnitudes = magnitudeCalculator.calculate(processedSamples)
        val granularity = granularityCalculator.calculate(magnitudes.size, audioFrame.format)

        return magnitudes.mapIndexed { index, magnitude ->
            frequencyBinFactory.create(index, granularity, magnitude)
        }.removeBinsBeyondNyquistFrequency(audioFrame.format)
    }

    private fun FrequencyBins.removeBinsBeyondNyquistFrequency(audioFormat: AudioFormat): FrequencyBins {
        return this.filter { it.frequency <= audioFormat.nyquistFrequency }
    }

}
