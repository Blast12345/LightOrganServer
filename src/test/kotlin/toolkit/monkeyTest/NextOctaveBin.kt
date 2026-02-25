package toolkit.monkeyTest

import sound.bins.octave.OctaveBin
import kotlin.random.Random

fun nextOctaveBin(
    position: Float = Random.nextFloat(),
    magnitude: Float = Random.nextFloat()
): OctaveBin {
    return OctaveBin(
        position = position,
        magnitude = magnitude
    )
}
