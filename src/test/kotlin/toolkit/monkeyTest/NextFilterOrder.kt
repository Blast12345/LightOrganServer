package toolkit.monkeyTest

import dsp.filtering.FilterOrder

fun nextFilterOrder(): FilterOrder {
    return FilterOrder(
        value = nextPositiveInt(min = 1)
    )
}