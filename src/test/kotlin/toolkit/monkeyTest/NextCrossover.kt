package toolkit.monkeyTest

import sound.bins.frequency.filters.Crossover
import kotlin.random.Random

fun nextCrossover(): Crossover {
    return Crossover(
        stopFrequency = Random.nextFloat(),
        cornerFrequency = Random.nextFloat()
    )
}