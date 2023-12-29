package sound.bins

import math.featureScaling.normalizeLogarithmically
import sound.frequencyBins.FrequencyBinList
import sound.notes.Notes
import sound.octave.OctaveBin
import sound.octave.OctaveBinList

// TODO: Move files
class FrequencyToOctaveConverter {

    private val rootNote = Notes.C

    fun convert(frequencyBins: FrequencyBinList): OctaveBinList {
        return frequencyBins.map {
            OctaveBin(
                position = it.frequency.normalizeToOctave() % 1,
                magnitude = it.magnitude
            )
        }
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