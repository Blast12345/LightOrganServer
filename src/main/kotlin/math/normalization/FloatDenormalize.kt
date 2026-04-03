package math.normalization

fun Float.denormalize(minimum: Float, maximum: Float): Float {
    return (this * (maximum - minimum)) + minimum
}
