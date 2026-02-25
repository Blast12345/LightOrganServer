package dsp.bins

import dsp.bins.frequency.FrequencyBin
import dsp.bins.notes.Note
import dsp.bins.notes.Notes
import dsp.bins.octave.OctaveBin
import math.featureScaling.normalizeLogarithmically

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
