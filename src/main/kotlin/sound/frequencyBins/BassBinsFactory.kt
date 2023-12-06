package sound.frequencyBins

import config.ConfigSingleton
import input.audioFrame.AudioFrame
import sound.frequencyBins.filters.PassBandRegionFilter
import sound.frequencyBins.listCalculator.FrequencyBinListCalculator

// TODO:
class BassBinsFactory(
    private val passBandRegionFilter: PassBandRegionFilter = PassBandRegionFilter()
) {

    fun create(audioFrame: AudioFrame): FrequencyBinList {
        val frequencyBins = FrequencyBinListCalculator().calculate(audioFrame.samples, audioFrame.format)
        val lowCrossover = ConfigSingleton.lowCrossover
        val highCrossover = ConfigSingleton.highCrossover

        return passBandRegionFilter.filter(
            frequencyBinList = frequencyBins,
            lowStopFrequency = lowCrossover.stopFrequency,
            highStopFrequency = highCrossover.stopFrequency
        )
    }

}