package toolkit.monkeyTest

fun nextFilterTopology(): FilterTopology {
    val frequency = nextPositiveInt().toFloat()

    return listOf(
        FilterTopology.LowPass(frequency),
        FilterTopology.HighPass(frequency),
    ).random()
}