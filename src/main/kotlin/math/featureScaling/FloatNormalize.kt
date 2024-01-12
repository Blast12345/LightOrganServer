package math.featureScaling

fun Float.normalize(minimum: Float, maximum: Float): Float {
    return (this - minimum) / (maximum - minimum)
}
