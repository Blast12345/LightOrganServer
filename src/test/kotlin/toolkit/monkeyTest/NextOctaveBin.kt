package toolkit.monkeyTest

import bins.octave.OctaveBin
import kotlin.random.Random

fun nextOctaveBin(
    octave: Int = Random.nextInt(),
    position: Float = Random.nextFloat(),
    magnitude: Float = Random.nextFloat()
): OctaveBin {
    return OctaveBin(
        octave = octave,
        position = position,
        magnitude = magnitude
    )
}
