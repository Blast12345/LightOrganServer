package sound.frequencyBins.bass

import config.Config
import config.ConfigSingleton
import input.audioFrame.AudioFrame
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.filters.BandPassFilter
import sound.frequencyBins.listCalculator.FrequencyBinListCalculator

class BassFrequencyBinListCalculator(
    private val frequencyBinListCalculator: FrequencyBinListCalculator = FrequencyBinListCalculator(),
    private val bandPassFilter: BandPassFilter = BandPassFilter(),
    private val config: Config = ConfigSingleton
) {

    fun calculate(audioFrame: AudioFrame): FrequencyBinList {
        return bandPassFilter.filter(
            frequencyBinList = getFrequencyBinList(audioFrame),
            lowCrossover = config.bassLowCrossover,
            highCrossover = config.bassHighCrossover
        )
    }

    private fun getFrequencyBinList(audioFrame: AudioFrame): FrequencyBinList {
        return frequencyBinListCalculator.calculate(audioFrame)
    }

}