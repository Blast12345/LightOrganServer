package math.featureScaling

open class LinearScale(
    private val minimum: Float,
    private val maximum: Float
) {

    private val range = (maximum - minimum)

    fun normalize(value: Float): Float {
        return (value - minimum) / (range)
    }

    fun scale(value: Float): Float {
        return (value * range) + minimum
    }

}