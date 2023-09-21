package sound.frequencyBins.bass

import input.audioFrame.AudioFrame
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.filters.BandPassFilter
import sound.frequencyBins.listCalculator.FrequencyBinListCalculator

class BassFrequencyBinListCalculator(
    private val frequencyBinListCalculator: FrequencyBinListCalculator = FrequencyBinListCalculator(),
    private val bandPassFilter: BandPassFilter = BandPassFilter()
) {

    fun calculate(audioFrame: AudioFrame): FrequencyBinList {
        return bandPassFilter.filter(
            frequencyBinList = getFrequencyBinList(audioFrame)
        )
    }

    private fun getFrequencyBinList(audioFrame: AudioFrame): FrequencyBinList {
        return frequencyBinListCalculator.calculate(audioFrame)
    }

}