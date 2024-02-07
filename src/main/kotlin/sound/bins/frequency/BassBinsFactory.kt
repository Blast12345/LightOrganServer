package sound.bins.frequency

import config.ConfigSingleton
import input.audioFrame.AudioFrame
import sound.bins.frequency.filters.Crossover
import sound.bins.frequency.filters.PassBandRegionFilter
import sound.bins.frequency.listCalculator.FrequencyBinsCalculator

class BassBinsFactory(
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator(),
    private val passBandRegionFilter: PassBandRegionFilter = PassBandRegionFilter(),
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
) {

    fun create(audioFrame: AudioFrame): FrequencyBins {
        return getFrequencyBins(audioFrame)
//        val frequencyBins = getFrequencyBins(audioFrame)
//
//        return passBandRegionFilter.filter(
//            frequencyBins = frequencyBins,
//            lowStopFrequency = lowCrossover.stopFrequency,
//            highStopFrequency = highCrossover.stopFrequency
//        )
    }

    private fun getFrequencyBins(audioFrame: AudioFrame): FrequencyBins {
        return frequencyBinsCalculator.calculate(
            samples = audioFrame.samples,
            audioFormat = audioFrame.format
        )
    }

}
