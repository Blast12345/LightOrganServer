package math.featureScaling

// Reference: https://en.wikipedia.org/wiki/Feature_scaling
open class MinMaxNormalization {

    fun normalize(value: Float, minimum: Float, maximum: Float): Float {
        return (value - minimum) / (maximum - minimum)
    }

    fun denormalize(value: Float, minimum: Float, maximum: Float): Float {
        val range = maximum - minimum
        return (value * range) + minimum
    }

}