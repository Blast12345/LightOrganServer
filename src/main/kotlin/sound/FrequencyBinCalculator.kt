package sound

import input.AudioFormat
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

    fun calculate(samples: FloatArray, audioFormat: AudioFormat): FrequencyBins {
        val doubleArraySamples = samples.map { it.toDouble() }.toDoubleArray()
        val processedSamples = signalProcessor.process(doubleArraySamples) // TODO: Change array type
        val magnitudes = magnitudeCalculator.calculate(processedSamples)
        val granularity = granularityCalculator.calculate(magnitudes.size, audioFormat)

        return magnitudes.mapIndexed { index, magnitude ->
            frequencyBinFactory.create(index, granularity, magnitude)
        }.removeBinsBeyondNyquistFrequency(audioFormat)
    }

    private fun FrequencyBins.removeBinsBeyondNyquistFrequency(audioFormat: AudioFormat): FrequencyBins {
        return this.filter { it.frequency <= audioFormat.nyquistFrequency }
    }

}
