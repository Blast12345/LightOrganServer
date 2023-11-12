package color

import color.stevensPowerLaw.HueScale
import color.stevensPowerLaw.NoteScale
import math.LogarithmicRescaler

class HueFactory(
    private val rescaler: LogarithmicRescaler = LogarithmicRescaler()
) {

    fun create(frequency: Float): Float {
        return rescaler.rescale(
            value = frequency,
            fromScale = NoteScale,
            toScale = HueScale,
        )
    }

}