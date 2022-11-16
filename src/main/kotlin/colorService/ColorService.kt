package colorService

import colorService.color.ColorGenerator
import colorService.color.ColorGeneratorInterface
import colorService.sound.FrequencyBinsService
import colorService.sound.FrequencyBinsServiceInterface
import java.awt.Color

typealias NextColor = (Color) -> Unit

interface ColorServiceInterface {
    fun listenForNextColor(lambda: NextColor)
}

class ColorService(private val frequencyBinService: FrequencyBinsServiceInterface = FrequencyBinsService(),
                   private val colorGenerator: ColorGeneratorInterface = ColorGenerator()): ColorServiceInterface {

    override fun listenForNextColor(lambda: NextColor) {
        frequencyBinService.listenForFrequencyBins { frequencyBins ->
            val color = colorGenerator.colorForFrequencyBins(frequencyBins) // TODO: Color Factory
            lambda(color)
        }
    }

}