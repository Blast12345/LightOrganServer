package color

class BrightnessFactory {

    fun create(magnitude: Float): Float {
        return if (magnitude < 1) {
            magnitude
        } else {
            1F
        }
    }

}
