package sound.frequencyBins

import sound.fft.RelativeMagnitudesCalculator
import sound.fft.RelativeMagnitudesCalculatorInterface
import sound.frequencyBins.filters.SupportedFrequencyBinsFilter
import sound.frequencyBins.filters.SupportedFrequencyBinsFilterInterface
import sound.input.samples.AudioSignal
import sound.signalProcessing.SignalProcessor
import sound.signalProcessing.SignalProcessorInterface

interface FrequencyBinsServiceInterface {
    fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList
}

class FrequencyBinsService(
    private val signalProcessor: SignalProcessorInterface = SignalProcessor(),
    private val relativeMagnitudesCalculator: RelativeMagnitudesCalculatorInterface = RelativeMagnitudesCalculator(),
    private val granularityCalculator: GranularityCalculatorInterface = GranularityCalculator(),
    private val frequencyBinListFactory: FrequencyBinListFactoryInterface = FrequencyBinListFactory(),
    private val supportedFrequencyBinsFilter: SupportedFrequencyBinsFilterInterface = SupportedFrequencyBinsFilter()
) : FrequencyBinsServiceInterface {

    private val lowestSupportedFrequency = 20F

    override fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        val processedSamples = getProcessedSamples(audioSignal)
        val magnitudes = getMagnitudes(processedSamples)
        val granularity = getGranularity(magnitudes.size, audioSignal.sampleRate)
        val allBins = getAllBins(magnitudes, granularity)
        return getSupportedBins(allBins)
    }

    private fun getProcessedSamples(audioSignal: AudioSignal): DoubleArray {
        return signalProcessor.process(audioSignal, lowestSupportedFrequency)
    }

    private fun getMagnitudes(signal: DoubleArray): DoubleArray {
        return relativeMagnitudesCalculator.calculate(signal)
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