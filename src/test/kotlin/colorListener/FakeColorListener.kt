package colorListener

class FakeColorListener: ColorListenerInterface {

    var lambda: NextColor? = null

    override fun listenForNextColor(lambda: NextColor) {
        this.lambda = lambda
    }

}