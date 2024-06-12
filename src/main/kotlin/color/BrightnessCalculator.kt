package color

import config.ConfigSingleton
import input.audioFrame.AudioFrame
import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.filters.Crossover
import sound.bins.frequency.listCalculator.FrequencyBinsCalculator
import sound.signalProcessor.FrequencyBinsRangeFilter
import sound.signalProcessor.SampleTrimmer

class BrightnessCalculator(
    private val sampleSize: Int = ConfigSingleton.brightnessSampleSize,
    private val lowCrossover: Crossover = ConfigSingleton.hueLowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.hueHighCrossover,
    private val sampleTrimmer: SampleTrimmer = SampleTrimmer(),
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator(),
    private val frequencyBinsRangeFilter: FrequencyBinsRangeFilter = FrequencyBinsRangeFilter()
) {

    fun calculate(audioFrame: AudioFrame): Float? {
        val trimmedSamples = sampleTrimmer.trim(audioFrame.samples, sampleSize)
        val frequencyBins = frequencyBinsCalculator.calculate(trimmedSamples, audioFrame.format)
        // TODO:
        val filteredFrequencyBins = frequencyBinsRangeFilter.filter(frequencyBins, lowCrossover.stopFrequency, highCrossover.stopFrequency)
        val maximumMagnitude = filteredFrequencyBins.maxOfOrNull { it.magnitude } ?: return null

        val brightness = maximumMagnitude * ConfigSingleton.brightnessMultiplier

        return if (brightness < 1F) {
            brightness
        } else {
            1F
        }
    }

}

class GreatestMagnitudeFinder {

    fun find(frequencyBins: FrequencyBins): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}

