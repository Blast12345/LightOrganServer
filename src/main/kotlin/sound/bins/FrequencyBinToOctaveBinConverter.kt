package sound.bins

import math.featureScaling.normalizeLogarithmically
import sound.bins.frequency.FrequencyBin
import sound.bins.octave.OctaveBin
import sound.notes.Note
import sound.notes.Notes

class FrequencyBinToOctaveBinConverter(
    private val rootNote: Note = Notes.C
) {

    fun convert(frequencyBin: FrequencyBin): OctaveBin {
        return OctaveBin(
            position = frequencyBin.frequency.normalizeToOctave() % 1,
            magnitude = frequencyBin.magnitude
        )
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