package colorService.color

import colorService.sound.FrequencyBin
import java.awt.Color

class FakeColorGenerator: ColorGeneratorInterface {

    var frequencyBins: List<FrequencyBin>? = null
    val color: Color = Color.orange

    override fun colorForFrequencyBins(frequencyBins: List<FrequencyBin>): Color {
        this.frequencyBins = frequencyBins
        return color
    }

}