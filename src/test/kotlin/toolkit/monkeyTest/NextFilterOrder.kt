package toolkit.monkeyTest

import dsp.filtering.config.FilterOrder

fun nextFilterOrder(): FilterOrder {
    return FilterOrder(
        value = nextPositiveInt(min = 1)
    )
}