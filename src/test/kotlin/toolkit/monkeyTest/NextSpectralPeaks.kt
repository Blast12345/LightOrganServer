package toolkit.monkeyTest

import dsp.peakExtraction.SpectralPeak
import dsp.peakExtraction.SpectralPeaks
import kotlin.random.Random

fun nextSpectralPeaks(
    length: Int = Random.nextInt(3, 10)
): SpectralPeaks {
    val list: MutableList<SpectralPeak> = mutableListOf()

    repeat(length) {
        list.add(nextSpectralPeak())
    }

    return list.toList()
}
