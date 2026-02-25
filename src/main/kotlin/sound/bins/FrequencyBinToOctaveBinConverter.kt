package sound.bins

import dsp.fft.FrequencyBin
import sound.bins.notes.Note
import sound.bins.notes.Notes
import sound.bins.octave.OctaveBin
import math.featureScaling.normalizeLogarithmically

class FrequencyBinToOctaveBinConverter(
    private val rootNote: sound.bins.notes.Note = _root_ide_package_.sound.bins.notes.Notes.C
) {

    fun convert(frequencyBin: FrequencyBin): sound.bins.octave.OctaveBin {
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
