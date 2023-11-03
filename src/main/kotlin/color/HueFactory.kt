package color

import color.stevensPowerLaw.HueScale
import color.stevensPowerLaw.LogarithmicRescaler
import color.stevensPowerLaw.MusicScale

class HueFactory(
    private val rescaler: LogarithmicRescaler = LogarithmicRescaler()
) {

    fun create(frequency: Float): Float {
        return rescaler.rescale(
            value = frequency,
            fromScale = MusicScale,
            toScale = HueScale,
        )
    }

}