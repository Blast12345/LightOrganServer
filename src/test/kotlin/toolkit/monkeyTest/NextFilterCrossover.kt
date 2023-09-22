package toolkit.monkeyTest

import sound.frequencyBins.filters.FilterCrossover
import kotlin.random.Random

fun nextFilterCrossover(): FilterCrossover {
    return FilterCrossover(
        stopFrequency = Random.nextFloat(),
        cornerFrequency = Random.nextFloat()
    )
}