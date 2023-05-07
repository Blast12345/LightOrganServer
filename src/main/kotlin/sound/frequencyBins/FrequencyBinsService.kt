package sound.frequencyBins

import input.audioFrame.AudioFrame
import sound.fft.RelativeMagnitudesCalculator
import sound.frequencyBins.filters.FrequencyBinListDenoiser
import sound.signalProcessing.SignalProcessor
import wrappers.audioFormat.AudioFormatWrapper

class FrequencyBinsService(
    private val signalProcessor: SignalProcessor = SignalProcessor(),
    private val relativeMagnitudesCalculator: RelativeMagnitudesCalculator = RelativeMagnitudesCalculator(),
    private val granularityCalculator: GranularityCalculator = GranularityCalculator(),
    private val frequencyBinListFactory: FrequencyBinListFactory = FrequencyBinListFactory(),
    private val frequencyBinListDenoiser: FrequencyBinListDenoiser = FrequencyBinListDenoiser()
) {

    fun getFrequencyBins(audioFrame: AudioFrame): FrequencyBinList {
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