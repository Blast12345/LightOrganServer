package toolkit.monkeyTest

import gui.dashboard.tiles.spectrum.SpectrumBin
import kotlin.random.Random

fun nextSpectrumBin(
    frequency: Float = Random.nextFloat(),
    magnitude: Float = Random.nextFloat()
): SpectrumBin {
    return SpectrumBin(
        frequency = frequency,
        magnitude = magnitude,
        hovered = Random.nextBoolean()
    )
}
