package toolkit.monkeyTest

import dsp.peakExtraction.SpectralPeak
import kotlin.random.Random

fun nextSpectralPeak(
    frequency: Float = Random.nextFloat(),
    magnitude: Float = Random.nextFloat(),
): SpectralPeak {
    return SpectralPeak(
        frequency = frequency,
        magnitude = magnitude
    )
}