package math

// Reference: https://en.wikipedia.org/wiki/Feature_scaling
class MinMaxNormalization {

    fun calculate(value: Float, minimum: Float, maximum: Float): Float {
        return (value - minimum) / (maximum - minimum)
    }

}