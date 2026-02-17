package toolkit.monkeyTest

import gui.tiles.spectrum.SpectrumBin
import kotlin.random.Random

fun nextSpectrumBin(): SpectrumBin {
    return SpectrumBin(
        frequency = Random.nextFloat(),
        magnitude = Random.nextFloat(),
        hovered = Random.nextBoolean()
    )
}
