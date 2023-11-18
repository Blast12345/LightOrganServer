package extensions

fun Float.denormalize(minimum: Float, maximum: Float): Float {
    return (this * (maximum - minimum)) + minimum
}