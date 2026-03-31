package toolkit.monkeyTest

import dsp.filtering.config.FilterConfig

fun nextHighPassConfig(): FilterConfig.HighPass {
    return FilterConfig.HighPass(
        family = nextFilterFamily(),
        frequency = nextPositiveFloat()
    )
}

fun nextLowPassConfig(): FilterConfig.LowPass {
    return FilterConfig.LowPass(
        family = nextFilterFamily(),
        frequency = nextPositiveFloat()
    )
}