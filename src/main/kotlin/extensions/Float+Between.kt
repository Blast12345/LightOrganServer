package extensions

fun Float.between(min: Float, max: Float): Float {
    return this.min(max).max(min)
}

private fun Float.min(value: Float): Float {
    return kotlin.math.min(value, this)
}

private fun Float.max(value: Float): Float {
    return kotlin.math.max(value, this)
}
