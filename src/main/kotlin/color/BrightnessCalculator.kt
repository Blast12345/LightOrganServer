package color

import input.audioFrame.AudioFrame
import organize.HardBandpassFilter
import organize.SampleTrimmer
import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.GreatestMagnitudeFinder
import sound.bins.frequency.filters.BandPassFilter
import sound.bins.frequency.filters.Crossover
import sound.bins.frequency.listCalculator.FrequencyBinsCalculator
import sound.notes.Notes

class BrightnessCalculator(
    private val bandPassFilter: BandPassFilter = BandPassFilter(),
    private val greatestMagnitudeFinder: GreatestMagnitudeFinder = GreatestMagnitudeFinder(),
    private val sampleTrimmer: SampleTrimmer = SampleTrimmer(),
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator(),
    private val hardBandpassFilter: HardBandpassFilter = HardBandpassFilter()
) {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        val magnitude = calculateMagnitude(frequencyBins) ?: return null

        return if (magnitude < 1F) {
            magnitude
        } else {
            1F
        }
    }

    private fun calculateMagnitude(frequencyBins: FrequencyBins): Float? {
        val filteredBins = getFilteredBins(frequencyBins)
        return greatestMagnitudeFinder.find(filteredBins)
    }

    private fun getFilteredBins(frequencyBins: FrequencyBins): FrequencyBins {
        return bandPassFilter.filter(
            frequencyBins = frequencyBins,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }


    private val sampleSize = 4410
    private val lowCrossover = Crossover(
        stopFrequency = Notes.C.getFrequency(1),
        cornerFrequency = Notes.C.getFrequency(2)
    )

    private val highCrossover = Crossover(
        cornerFrequency = Notes.C.getFrequency(2),
        stopFrequency = Notes.C.getFrequency(3)
    )


    fun calculate(audioFrame: AudioFrame): Float? {
        val trimmedSamples = sampleTrimmer.trim(audioFrame.samples, sampleSize)
        val frequencyBins = frequencyBinsCalculator.calculate(trimmedSamples, audioFrame.format)
        val filteredFrequencyBins = hardBandpassFilter.filter(frequencyBins, lowCrossover, highCrossover)
        return filteredFrequencyBins.maxOfOrNull { it.magnitude }
    }

}
