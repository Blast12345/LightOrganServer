package colorService

typealias NextColor = (String) -> Unit

interface ColorServiceInterface {
    fun listenForNextColor(lambda: NextColor)
}

class ColorService: ColorServiceInterface {

    override fun listenForNextColor(lambda: NextColor) {
        while (true) {
            lambda("ASDF")
        }
    }

}