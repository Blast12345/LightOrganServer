package toolkit.monkeyTest

import dsp.filtering.config.FilterFamily

fun nextFilterFamily(): FilterFamily {
    val order = nextFilterOrder()

    return listOf(
        FilterFamily.Butterworth(order)
    ).random()
}