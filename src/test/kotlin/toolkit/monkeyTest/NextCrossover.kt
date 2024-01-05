package toolkit.monkeyTest

import sound.bins.frequencyBins.filters.Crossover
import kotlin.random.Random

fun nextCrossover(): Crossover {
    return Crossover(
        stopFrequency = Random.nextFloat(),
        cornerFrequency = Random.nextFloat()
    )
}