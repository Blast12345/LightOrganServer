package sound.frequencyBins.listCalculator

import input.audioFrame.AudioFrame
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.FrequencyBinListFactory

class FrequencyBinListCalculator(
    private val magnitudeListCalculator: MagnitudeListCalculator = MagnitudeListCalculator(),
    private val granularityCalculator: GranularityCalculator = GranularityCalculator(),
    private val frequencyBinListFactory: FrequencyBinListFactory = FrequencyBinListFactory()
) {

    fun calculate(audioFrame: AudioFrame): FrequencyBinList {
        val magnitudes = getMagnitudes(audioFrame)
        val granularity = getGranularity(magnitudes.size, audioFrame)
        return frequencyBinListFactory.create(magnitudes, granularity)
    }

    private fun getMagnitudes(audioFrame: AudioFrame): DoubleArray {
        return magnitudeListCalculator.calculate(audioFrame.samples)
    }

    private fun getGranularity(numberOfBins: Int, audioFrame: AudioFrame): Float {
        return granularityCalculator.calculate(numberOfBins, audioFrame.format)
    }

}