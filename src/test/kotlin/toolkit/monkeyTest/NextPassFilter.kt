package toolkit.monkeyTest

import sound.frequencyBins.filters.PassFilter
import kotlin.random.Random

fun nextPassFilter(): PassFilter {
    return PassFilter(
        stopFrequency = Random.nextFloat(),
        cornerFrequency = Random.nextFloat()
    )
}