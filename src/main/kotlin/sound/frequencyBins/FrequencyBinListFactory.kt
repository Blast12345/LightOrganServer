package sound.frequencyBins

import sound.input.samples.AudioSignal
import sound.signalProcessing.*

interface FrequencyBinListFactoryInterface {
    fun create(audioSignal: AudioSignal): FrequencyBinList
}

class FrequencyBinListFactory(
    private val signalProcessor: SignalProcessorInterface = SignalProcessor(),
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm(),
    private val magnitudeNormalizer: MagnitudeNormalizerInterface = MagnitudeNormalizer(),
    private val frequencyBinFactory: FrequencyBinFactoryInterface = FrequencyBinFactory()
) : FrequencyBinListFactoryInterface {

    private val lowestSupportedFrequency = 20F

    override fun create(audioSignal: AudioSignal): FrequencyBinList {
        val processedSamples = getProcessedSamples(audioSignal)
        val magnitudes = getMagnitudes(processedSamples)
        val normalizedMagnitudes = getNormalizedMagnitudes(magnitudes, processedSamples.size)
        val granularity = calculateGranularity(normalizedMagnitudes.size, audioSignal.sampleRate)
        val allBins = createAllBins(normalizedMagnitudes, granularity)
        return getSupportedBins(allBins)
    }

    private fun getProcessedSamples(audioSignal: AudioSignal): DoubleArray {
        return signalProcessor.process(audioSignal, lowestSupportedFrequency)
    }

    private fun getMagnitudes(processedSamples: DoubleArray): DoubleArray {
        return fftAlgorithm.calculateMagnitudes(processedSamples)
    }

    private fun getNormalizedMagnitudes(magnitudes: DoubleArray, sampleSize: Int): DoubleArray {
        return magnitudeNormalizer.normalize(magnitudes, sampleSize)
    }

    private fun calculateGranularity(numberOfBins: Int, sampleRate: Float): Float {
        val nyquistFrequency = calculateNyquistFrequency(sampleRate)
        return numberOfBins / nyquistFrequency
    }

    private fun calculateNyquistFrequency(sampleRate: Float): Float {
        return sampleRate / 2
    }

    private fun createAllBins(magnitudes: DoubleArray, granularity: Float): FrequencyBinList {
        val frequencyBins: MutableList<FrequencyBin> = mutableListOf()

        magnitudes.forEachIndexed { index, magnitude ->
            val frequencyBin = frequencyBinFactory.create(index, granularity, magnitude)
            frequencyBins.add(frequencyBin)
        }

        return frequencyBins
    }

    private fun getSupportedBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.filter { it.frequency >= lowestSupportedFrequency }
    }

}