package toolkit.monkeyTest

import dsp.filtering.FilterOrder

fun nextFilterOrder(): FilterOrder {
    return FilterOrder(
        value = nextInt(min = 1)
    )
}