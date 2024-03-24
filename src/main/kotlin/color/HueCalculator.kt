package color

import input.audioFrame.AudioFrame
import organize.HardBandpassFilter
import organize.SampleTrimmer
import sound.bins.FrequencyBinsToOctaveBinsConverter
import sound.bins.frequency.dominant.frequency.PeakFrequencyBinsFinder
import sound.bins.frequency.filters.Crossover
import sound.bins.frequency.listCalculator.FrequencyBinsCalculator
import sound.bins.octave.OctaveWeightedAverageCalculator
import sound.notes.Notes

class HueCalculator(
    private val sampleTrimmer: SampleTrimmer = SampleTrimmer(),
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator(),
    private val hardBandpassFilter: HardBandpassFilter = HardBandpassFilter(),
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val frequencyBinsToOctaveBinsConverter: FrequencyBinsToOctaveBinsConverter = FrequencyBinsToOctaveBinsConverter(),
    private val octaveWeightedAverageCalculator: OctaveWeightedAverageCalculator = OctaveWeightedAverageCalculator(),
) {

    private val sampleSize = 8820
    private val lowCrossover = Crossover(
        stopFrequency = Notes.C.getFrequency(0),
        cornerFrequency = Notes.C.getFrequency(1)
    )

    private val highCrossover = Crossover(
        cornerFrequency = Notes.C.getFrequency(2),
        stopFrequency = Notes.C.getFrequency(3)
    )


    fun calculate(audioFrame: AudioFrame): Float? {
        val trimmedSamples = sampleTrimmer.trim(audioFrame.samples, sampleSize)
        val frequencyBins = frequencyBinsCalculator.calculate(trimmedSamples, audioFrame.format)
        val filteredFrequencyBins = hardBandpassFilter.filter(frequencyBins, lowCrossover, highCrossover)
        val peakFrequencyBins = peakFrequencyBinsFinder.find(filteredFrequencyBins)
        val peakOctaveBins = frequencyBinsToOctaveBinsConverter.convert(peakFrequencyBins)
        val octaveWeightedAverage = octaveWeightedAverageCalculator.calculate(peakOctaveBins)
        return octaveWeightedAverage
    }

}
