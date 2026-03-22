package toolkit.monkeyTest

import dsp.filtering.config.FilterConfig
import dsp.filtering.config.FilterFamily

fun nextFilterConfig(): FilterConfig {
    return FilterConfig(
        family = nextEnum<FilterFamily>(),
        topology = nextFilterTopology(),
        order = nextPositiveInt()
    )
}