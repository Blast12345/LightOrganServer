package sound.bins

import dsp.bins.FrequencyBin
import music.TuningSystem
import music.WesternTuningSystem
import sound.bins.octave.OctaveBin

class FrequencyBinToOctaveBinConverter(
    private val tuning: TuningSystem = WesternTuningSystem()
) {

    fun convert(frequencyBin: FrequencyBin): OctaveBin {
        return OctaveBin(
            position = tuning.getPositionInOctave(frequencyBin.frequency).turns.toFloat(),
            magnitude = frequencyBin.magnitude
        )
    }

}
