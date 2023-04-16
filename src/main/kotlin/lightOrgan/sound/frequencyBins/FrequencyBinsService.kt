package lightOrgan.sound.frequencyBins

import input.samples.AudioSignal
import lightOrgan.sound.fft.RelativeMagnitudesCalculator
import lightOrgan.sound.fft.RelativeMagnitudesCalculatorInterface
import lightOrgan.sound.frequencyBins.filters.FrequencyBinListDenoiser
import lightOrgan.sound.frequencyBins.filters.FrequencyBinListDenoiserInterface
import lightOrgan.sound.signalProcessing.SignalProcessor
import lightOrgan.sound.signalProcessing.SignalProcessorInterface
import wrappers.audioFormat.AudioFormatWrapper

interface FrequencyBinsServiceInterface {
    fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList
}

class FrequencyBinsService(
    private val signalProcessor: SignalProcessorInterface = SignalProcessor(),
    private val relativeMagnitudesCalculator: RelativeMagnitudesCalculatorInterface = RelativeMagnitudesCalculator(),
    private val granularityCalculator: GranularityCalculatorInterface = GranularityCalculator(),
    private val frequencyBinListFactory: FrequencyBinListFactoryInterface = FrequencyBinListFactory(),
    private val frequencyBinListDenoiser: FrequencyBinListDenoiserInterface = FrequencyBinListDenoiser()
) : FrequencyBinsServiceInterface {

    override fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        val magnitudes = getMagnitudes(audioSignal)
        val granularity = getGranularity(magnitudes.size, audioSignal.format)
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