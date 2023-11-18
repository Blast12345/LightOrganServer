package color.scales

import extensions.denormalize

class HueScale {

    private val minimum = 0F
    private val maximum = 1F

    fun scale(normalizedValue: Float): Float {
        return normalizedValue
            .denormalize(minimum, maximum)
            .loopIntoRange()
    }

    private fun Float.loopIntoRange(): Float {
        return if (this < minimum) {
            (this % 1) + (maximum - minimum)
        } else {
            this % 1
        }
    }

}