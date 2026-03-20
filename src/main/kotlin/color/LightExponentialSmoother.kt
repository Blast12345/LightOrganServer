package color

import math.smoothing.ExponentialSmoother
import kotlin.time.Duration

// TODO: Rename? Test?
class LightExponentialSmoother(
    private val halfLife: Duration
) {

    private val smoother = ExponentialSmoother(
        halfLife = halfLife,
        zero = Light(),
        scale = { light, factor -> light * factor },
        add = { a, b -> a + b }
    )

    fun smooth(light: Light): Light {
        return smoother.smooth(light)
    }

}


