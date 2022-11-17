package colorListener

import colorListener.color.ColorFactory
import colorListener.color.ColorFactoryInterface
import colorListener.sound.FrequencyBinsService
import colorListener.sound.FrequencyBinsServiceInterface
import java.awt.Color

typealias NextColor = (Color) -> Unit

interface ColorListenerInterface {
    fun listenForNextColor(lambda: NextColor)
}

class ColorListener(
    private val frequencyBinService: FrequencyBinsServiceInterface = FrequencyBinsService(),
    private val colorFactory: ColorFactoryInterface = ColorFactory()
) : ColorListenerInterface {

    override fun listenForNextColor(lambda: NextColor) {
        frequencyBinService.listenForFrequencyBins { frequencyBins ->
            val color = colorFactory.colorFrom(frequencyBins)
            lambda(color)
        }
    }

}