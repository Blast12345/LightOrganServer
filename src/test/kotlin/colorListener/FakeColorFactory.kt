package colorListener

import java.awt.Color

class FakeColorFactory : ColorListenerInterface {

    var nextColor: NextColor? = null
    var sample: DoubleArray? = null
    val color: Color = Color.blue

    override fun listenForNextColor(lambda: NextColor) {
        this.nextColor = lambda
    }

    override fun colorFor(sample: DoubleArray): Color {
        this.sample = sample
        return color
    }

}