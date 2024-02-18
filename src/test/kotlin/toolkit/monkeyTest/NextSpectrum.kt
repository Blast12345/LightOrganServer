package toolkit.monkeyTest

import gui.dashboard.tiles.spectrum.Spectrum
import gui.dashboard.tiles.spectrum.SpectrumBin
import kotlin.random.Random

fun nextSpectrum(
    length: Int = Random.nextInt(1, 10)
): Spectrum {
    val list: MutableList<SpectrumBin> = mutableListOf()

    repeat(length) {
        list.add(nextSpectrumBin())
    }

    return list.toList()
}

