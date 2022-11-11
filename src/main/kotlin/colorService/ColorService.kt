package colorService

import colorService.color.ColorGenerator
import colorService.color.ColorGeneratorInterface
import colorService.color.FrequencyBinService
import colorService.color.FrequencyBinServiceInterface
import java.awt.Color

typealias NextColor = (Color) -> Unit

interface ColorServiceInterface {
    fun listenForNextColor(lambda: NextColor)
}

class ColorService(private val frequencyBinService: FrequencyBinServiceInterface = FrequencyBinService(),
                   private val colorGenerator: ColorGeneratorInterface = ColorGenerator()): ColorServiceInterface {

    override fun listenForNextColor(lambda: NextColor) {
        frequencyBinService.listenForFrequencyBins { frequencyBins ->
            val color = colorGenerator.colorForFrequency(frequencyBins)
            lambda(color)
        }
    }

}