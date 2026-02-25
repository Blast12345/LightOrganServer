package toolkit.monkeyTest

import dsp.bins.frequency.filters.Crossover
import kotlin.random.Random

fun nextCrossover(): Crossover {
    return Crossover(
        stopFrequency = Random.nextFloat(),
        cornerFrequency = Random.nextFloat()
    )
}
