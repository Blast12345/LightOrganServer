package colorListener

import colorListener.color.ColorFactory
import colorListener.color.ColorFactoryInterface
import colorListener.sound.FrequencyBinsListener
import colorListener.sound.FrequencyBinsListenerInterface
import java.awt.Color

typealias NextColor = (Color) -> Unit

interface ColorListenerInterface {
    fun listenForNextColor(lambda: NextColor)
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

}