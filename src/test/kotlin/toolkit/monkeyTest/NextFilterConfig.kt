package toolkit.monkeyTest

import dsp.filtering.config.FilterConfig

fun nextFilterConfig(): FilterConfig {
    return FilterConfig(
        family = nextFilterFamily(),
        topology = nextFilterTopology()
    )
}


