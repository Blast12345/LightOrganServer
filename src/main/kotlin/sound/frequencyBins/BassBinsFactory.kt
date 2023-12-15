package sound.frequencyBins

import config.ConfigSingleton
import input.audioFrame.AudioFrame
import sound.frequencyBins.filters.Crossover
import sound.frequencyBins.filters.PassBandRegionFilter
import sound.frequencyBins.listCalculator.FrequencyBinListCalculator

class BassBinsFactory(
    private val frequencyBinListCalculator: FrequencyBinListCalculator = FrequencyBinListCalculator(),
    private val passBandRegionFilter: PassBandRegionFilter = PassBandRegionFilter(),
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
) {

    fun create(audioFrame: AudioFrame): FrequencyBinList {
        val frequencyBins = getFrequencyBins(audioFrame)

        return passBandRegionFilter.filter(
            frequencyBinList = frequencyBins,
            lowStopFrequency = lowCrossover.stopFrequency,
            highStopFrequency = highCrossover.stopFrequency
        )
    }

    private fun getFrequencyBins(audioFrame: AudioFrame): FrequencyBinList {
        return frequencyBinListCalculator.calculate(audioFrame.samples, audioFrame.format)
    }

}