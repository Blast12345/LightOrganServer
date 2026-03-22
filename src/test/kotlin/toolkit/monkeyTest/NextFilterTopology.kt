package toolkit.monkeyTest

import dsp.filtering.config.FilterTopology

fun nextFilterTopology(): FilterTopology {
    val frequency = nextPositiveInt().toFloat()

    return listOf(
        FilterTopology.LowPass(frequency),
        FilterTopology.HighPass(frequency),
    ).random()
}