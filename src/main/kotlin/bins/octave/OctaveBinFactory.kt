package bins.octave

import bins.frequency.FrequencyBin
import math.featureScaling.normalizeLogarithmically
import sound.notes.Notes

class OctaveBinFactory(
    // Frequency at the start of the zero octave
    private val rootFrequency: Float = Notes.C.getFrequency(0)
) {

    fun create(frequencyBin: FrequencyBin): OctaveBin {
        val continuousOctave = frequencyBin.frequency.normalizeLogarithmically(
            minimum = rootFrequency,
            maximum = rootFrequency * 2,
            base = 2F
        )

        return OctaveBin(
            octave = continuousOctave.toInt(),
            position = continuousOctave.mod(1f),
            magnitude = frequencyBin.magnitude
        )
    }

}