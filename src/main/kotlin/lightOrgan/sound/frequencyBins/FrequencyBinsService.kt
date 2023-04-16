package lightOrgan.sound.frequencyBins

import input.audioFrame.AudioFrame
import lightOrgan.sound.fft.RelativeMagnitudesCalculator
import lightOrgan.sound.fft.RelativeMagnitudesCalculatorInterface
import lightOrgan.sound.frequencyBins.filters.FrequencyBinListDenoiser
import lightOrgan.sound.frequencyBins.filters.FrequencyBinListDenoiserInterface
import lightOrgan.sound.signalProcessing.SignalProcessor
import lightOrgan.sound.signalProcessing.SignalProcessorInterface
import wrappers.audioFormat.AudioFormatWrapper

interface FrequencyBinsServiceInterface {
    fun getFrequencyBins(audioFrame: AudioFrame): FrequencyBinList
}

class FrequencyBinsService(
    private val signalProcessor: SignalProcessorInterface = SignalProcessor(),
    private val relativeMagnitudesCalculator: RelativeMagnitudesCalculatorInterface = RelativeMagnitudesCalculator(),
    private val granularityCalculator: GranularityCalculatorInterface = GranularityCalculator(),
    private val frequencyBinListFactory: FrequencyBinListFactoryInterface = FrequencyBinListFactory(),
    private val frequencyBinListDenoiser: FrequencyBinListDenoiserInterface = FrequencyBinListDenoiser()
) : FrequencyBinsServiceInterface {

    override fun getFrequencyBins(audioFrame: AudioFrame): FrequencyBinList {
        val magnitudes = getMagnitudes(audioFrame)
        val granularity = getGranularity(magnitudes.size, audioFrame.format)
        val frequencyBins = getFrequencyBins(magnitudes, granularity)
        return getDenoisedBins(frequencyBins)
    }

    private fun getMagnitudes(audioFrame: AudioFrame): DoubleArray {
        val processedSamples = getProcessedSamples(audioFrame)
        return relativeMagnitudesCalculator.calculate(processedSamples)
    }

    private fun getProcessedSamples(audioFrame: AudioFrame): DoubleArray {
        return signalProcessor.process(audioFrame)
    }

    private fun getGranularity(numberOfBins: Int, format: AudioFormatWrapper): Float {
        return granularityCalculator.calculate(numberOfBins, format.sampleRate, format.numberOfChannels)
    }

    private fun getFrequencyBins(magnitudes: DoubleArray, granularity: Float): FrequencyBinList {
        return frequencyBinListFactory.create(magnitudes, granularity)
    }

    private fun getDenoisedBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBinListDenoiser.denoise(frequencyBins)
    }

}