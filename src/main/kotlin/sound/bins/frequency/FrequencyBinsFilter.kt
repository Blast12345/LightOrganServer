package sound.bins.frequency

import config.ConfigSingleton
import dsp.bins.frequency.filters.Crossover
import dsp.bins.frequency.filters.PassBandRegionFilter
import dsp.fft.FrequencyBins

class FrequencyBinsFilter(
    private val passBandRegionFilter: PassBandRegionFilter = PassBandRegionFilter(),
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
) {

    fun filter(frequencyBins: FrequencyBins): FrequencyBins {
        return passBandRegionFilter.filter(
            frequencyBins = frequencyBins,
            lowStopFrequency = lowCrossover.stopFrequency,
            highStopFrequency = highCrossover.stopFrequency
        )
    }

}
