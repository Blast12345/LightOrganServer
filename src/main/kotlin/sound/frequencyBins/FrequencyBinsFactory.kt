package sound.frequencyBins

import sound.input.samples.AudioSignal
import sound.signalProcessing.*

interface FrequencyBinsFactoryInterface {
    fun create(audioSignal: AudioSignal): FrequencyBins
}

class FrequencyBinsFactory(
    private val signalProcessor: SignalProcessorInterface = SignalProcessor(),
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm(),
    private val magnitudeNormalizer: MagnitudeNormalizerInterface = MagnitudeNormalizer(),
    private val frequencyBinFactory: FrequencyBinFactoryInterface = FrequencyBinFactory()
) : FrequencyBinsFactoryInterface {

    private val lowestSupportedFrequency = 20F

    override fun create(audioSignal: AudioSignal): FrequencyBins {
        val processedSamples = signalProcessor.process(audioSignal)
        val magnitudes = fftAlgorithm.calculateMagnitudes(processedSamples)
        val normalizedMagnitudes = magnitudeNormalizer.normalize(magnitudes, processedSamples.size)
        val granularity = calculateGranularity(normalizedMagnitudes.size, audioSignal.sampleRate)
        return create(normalizedMagnitudes, granularity)
    }

    private fun create(magnitudes: DoubleArray, granularity: Float): FrequencyBins {
        val frequencyBins: MutableList<FrequencyBin> = mutableListOf()

        magnitudes.forEachIndexed { index, magnitude ->
            val frequencyBin = frequencyBinFactory.create(index, granularity, magnitude)
            frequencyBins.add(frequencyBin)
        }

        return frequencyBins
    }

    private fun calculateGranularity(numberOfBins: Int, sampleRate: Float): Float {
        val nyquistFrequency = calculateNyquistFrequency(sampleRate)
        return numberOfBins / nyquistFrequency
    }

    private fun calculateNyquistFrequency(sampleRate: Float): Float {
        return sampleRate / 2
    }

}