package sound.octave

import math.featureScaling.normalizeLogarithmically
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.dominant.frequency.PeakFrequencyBinsFinder
import sound.notes.Notes

class PeakOctaveBinsFinder(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder()
) {

    private val rootNote = Notes.C

    fun find(frequencyBins: FrequencyBinList): OctaveBinList {
        val peakBins = getPeakFrequencyBins(frequencyBins)

        return peakBins.map {
            OctaveBin(
                position = it.frequency.normalizeToOctave() % 1,
                magnitude = it.magnitude
            )
        }

    }

    private fun getPeakFrequencyBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return peakFrequencyBinsFinder.find(frequencyBins)
    }

    // Reference: https://en.wikipedia.org/wiki/Octave
    private fun Float.normalizeToOctave(): Float {
        return this.normalizeLogarithmically(
            minimum = rootNote.getFrequency(0),
            maximum = rootNote.getFrequency(1),
            base = 2F
        )
    }

}