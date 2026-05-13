package toolkit.monkeyTest

import dsp.bins.FrequencyBin
import org.apache.commons.math3.complex.Complex
import kotlin.random.Random

fun nextFrequencyBin(
    frequency: Float = Random.nextFloat(),
    value: Complex = nextComplex()
): FrequencyBin {
    return FrequencyBin(
        frequency = frequency,
        value = value
    )
}