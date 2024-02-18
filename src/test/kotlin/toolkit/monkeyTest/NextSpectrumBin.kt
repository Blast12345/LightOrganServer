package toolkit.monkeyTest

import gui.dashboard.tiles.spectrum.SpectrumBin
import kotlin.random.Random

fun nextSpectrumBin(): SpectrumBin {
    return SpectrumBin(
        frequency = Random.nextFloat(),
        magnitude = Random.nextFloat(),
        hovered = Random.nextBoolean()
    )
}
