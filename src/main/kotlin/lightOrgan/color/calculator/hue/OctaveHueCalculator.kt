package lightOrgan.color.calculator.hue

import bins.frequency.FrequencyBins
import bins.frequency.PeakFrequencyBinsCalculator
import bins.octave.OctaveBinFactory
import bins.octave.OctaveBins
import config.ConfigSingleton
import sound.bins.frequency.filters.Crossover

class OctaveHueCalculator(
    private val highCrossover: Crossover? = ConfigSingleton.highCrossover,
    private val peakFrequencyBinsCalculator: PeakFrequencyBinsCalculator = PeakFrequencyBinsCalculator(),
    private val octaveBinFactory: OctaveBinFactory = OctaveBinFactory()
) : HueCalculator {

    override fun calculate(frequencyBins: FrequencyBins): Float? {
        val filteredBins = frequencyBins.belowCrossover()
        val peakFrequencyBins = peakFrequencyBinsCalculator.calculate(filteredBins)
        val peakOctaveBins = peakFrequencyBins.map { octaveBinFactory.create(it) }

        val weightedMagnitude = weightedMagnitude(peakOctaveBins)
        val totalMagnitude = totalMagnitude(peakOctaveBins)

        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
    }

    private fun FrequencyBins.belowCrossover(): FrequencyBins {
        // Hard cutoff — a roll-off would attenuate peaks near the boundary,
        // distorting their relative magnitudes and skewing the hue calculation.
        val cutoffFrequency = highCrossover?.stopFrequency ?: Float.MAX_VALUE
        return this.filter { it.frequency <= cutoffFrequency }
    }

    private fun weightedMagnitude(octaveBins: OctaveBins): Float {
        var weightedMagnitude = 0F

        for (octaveBin in octaveBins) {
            weightedMagnitude += octaveBin.position * octaveBin.magnitude
        }

        return weightedMagnitude
    }

    private fun totalMagnitude(octaveBins: OctaveBins): Float {
        return octaveBins.map { it.magnitude }.sum()
    }

}