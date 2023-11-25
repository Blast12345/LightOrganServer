package color

class BrightnessFactory {

    // TODO: Improve logic around really low values. There is a lot of flickering.
    fun create(magnitude: Float): Float {
        return if (magnitude < 1) {
            magnitude
        } else {
            1F
        }
    }

}