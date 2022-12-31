package colorListener

import colorListener.color.ColorFactory
import colorListener.color.ColorFactoryInterface
import sound.frequencyBins.FrequencyBinsListener
import sound.frequencyBins.FrequencyBinsListenerInterface
import java.awt.Color

typealias NextColor = (Color) -> Unit

interface ColorListenerInterface {
    fun listenForNextColor(lambda: NextColor)
    fun colorFor(sample: DoubleArray): Color
}

class ColorListener(
    private val frequencyBinService: FrequencyBinsListenerInterface = FrequencyBinsListener(),
    private val colorFactory: ColorFactoryInterface = ColorFactory()
) : ColorListenerInterface {

    override fun listenForNextColor(lambda: NextColor) {
        frequencyBinService.listenForFrequencyBins { frequencyBins ->
            val color = colorFactory.colorFrom(frequencyBins)
            lambda(color)
        }
    }

    override fun colorFor(sample: DoubleArray): Color {
        TODO("Not yet implemented")
    }

}