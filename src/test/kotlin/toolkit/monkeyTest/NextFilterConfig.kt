package toolkit.monkeyTest

import dsp.filtering.FilterConfig
import dsp.filtering.FilterType

fun nextHighPassConfig(): FilterConfig {
    return FilterConfig(
        type = FilterType.HighPass(nextPositiveFloat()),
        family = nextFilterFamily(),
    )
}

fun nextLowPassConfig(): FilterConfig {
    return FilterConfig(
        type = FilterType.LowPass(nextPositiveFloat()),
        family = nextFilterFamily(),
    )
}
