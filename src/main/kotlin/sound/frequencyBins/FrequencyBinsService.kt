package sound.frequencyBins

import config.Config
import sound.fft.RelativeMagnitudesCalculator
import sound.fft.RelativeMagnitudesCalculatorInterface
import sound.frequencyBins.filters.FrequencyBinListDenoiser
import sound.frequencyBins.filters.FrequencyBinListDenoiserInterface
import sound.input.samples.AudioSignal
import sound.signalProcessing.SignalProcessor
import sound.signalProcessing.SignalProcessorInterface

interface FrequencyBinsServiceInterface {
    fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList
}

class FrequencyBinsService(
    config: Config,
    private val signalProcessor: SignalProcessorInterface = SignalProcessor(config),
    private val relativeMagnitudesCalculator: RelativeMagnitudesCalculatorInterface = RelativeMagnitudesCalculator(),
    private val granularityCalculator: GranularityCalculatorInterface = GranularityCalculator(),
    private val frequencyBinListFactory: FrequencyBinListFactoryInterface = FrequencyBinListFactory(),
    private val frequencyBinListDenoiser: FrequencyBinListDenoiserInterface = FrequencyBinListDenoiser()
) : FrequencyBinsServiceInterface {

    override fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        val magnitudes = getMagnitudes(audioSignal)
        val granularity = getGranularity(magnitudes.size, audioSignal.sampleRate)
        val frequencyBins = getFrequencyBins(magnitudes, granularity)
        return getDenoisedBins(frequencyBins)
    }

    private fun getMagnitudes(audioSignal: AudioSignal): DoubleArray {
        val processedSamples = getProcessedSamples(audioSignal)
        return relativeMagnitudesCalculator.calculate(processedSamples)
    }

    private fun getProcessedSamples(audioSignal: AudioSignal): DoubleArray {
        return signalProcessor.process(audioSignal)
    }

    private fun getGranularity(numberOfBins: Int, sampleRate: Float): Float {
        return granularityCalculator.calculate(numberOfBins, sampleRate)
    }

    private fun getFrequencyBins(magnitudes: DoubleArray, granularity: Float): FrequencyBinList {
        return frequencyBinListFactory.create(magnitudes, granularity)
    }

    private fun getDenoisedBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBinListDenoiser.denoise(frequencyBins)
    }

}