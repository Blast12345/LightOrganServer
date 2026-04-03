package sound.bins

import dsp.bins.FrequencyBin
import music.Tuning
import music.WesternTuning
import sound.bins.octave.OctaveBin

class FrequencyBinToOctaveBinConverter(
    private val tuning: Tuning = WesternTuning()
) {

    fun convert(frequencyBin: FrequencyBin): OctaveBin {
        return OctaveBin(
            position = tuning.getPositionInOctave(frequencyBin.frequency).turns.toFloat(),
            magnitude = frequencyBin.magnitude
        )
    }

}
