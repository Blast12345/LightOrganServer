package math.featureScaling

import kotlin.math.log

fun Float.normalizeLogarithmically(minimum: Float, maximum: Float, base: Float): Float {
    return log(this, base).normalize(
        minimum = log(minimum, base),
        maximum = log(maximum, base),
    )
}
