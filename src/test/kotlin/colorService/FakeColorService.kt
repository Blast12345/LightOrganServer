package colorService

class FakeColorService: ColorServiceInterface {

    var lambda: NextColor? = null

    override fun listenForNextColor(lambda: NextColor) {
        this.lambda = lambda
    }

}