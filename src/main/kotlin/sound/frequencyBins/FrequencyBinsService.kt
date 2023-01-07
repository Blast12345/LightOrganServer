package sound.frequencyBins

import sound.input.samples.AudioSignal
import sound.signalProcessing.*

interface FrequencyBinsServiceInterface {
    fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList
}

class FrequencyBinsService(
    private val signalProcessor: SignalProcessorInterface = SignalProcessor(),
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm(),
    private val magnitudeNormalizer: MagnitudeNormalizerInterface = MagnitudeNormalizer(),
    private val granularityCalculator: GranularityCalculatorInterface = GranularityCalculator(),
    private val frequencyBinListFactory: FrequencyBinListFactoryInterface = FrequencyBinListFactory(),
    private val supportedFrequencyBinsFilter: SupportedFrequencyBinsFilterInterface = SupportedFrequencyBinsFilter()
) : FrequencyBinsServiceInterface {

    private val lowestSupportedFrequency = 20F

    override fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        val processedSamples = getProcessedSamples(audioSignal)
        val magnitudes = getMagnitudes(processedSamples)
        val normalizedMagnitudes = getNormalizedMagnitudes(magnitudes, processedSamples.size)
        val granularity = getGranularity(normalizedMagnitudes.size, audioSignal.sampleRate)
        val allBins = getAllBins(normalizedMagnitudes, granularity)
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

    private fun getGranularity(numberOfBins: Int, sampleRate: Float): Float {
        return granularityCalculator.calculate(numberOfBins, sampleRate)
    }

    private fun getAllBins(magnitudes: DoubleArray, granularity: Float): FrequencyBinList {
        return frequencyBinListFactory.create(magnitudes, granularity)
    }

    private fun getSupportedBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return supportedFrequencyBinsFilter.filter(frequencyBins, lowestSupportedFrequency)
    }

}