package toolkit.monkeyTest

import org.apache.commons.math3.complex.Complex
import kotlin.random.Random

fun nextComplex(): Complex {
    return Complex(Random.nextDouble(), Random.nextDouble())
}