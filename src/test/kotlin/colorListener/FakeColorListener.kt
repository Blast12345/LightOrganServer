package colorListener

class FakeColorListener : ColorListenerInterface {

    var nextColor: NextColor? = null

    override fun listenForNextColor(lambda: NextColor) {
        this.nextColor = lambda
    }

}