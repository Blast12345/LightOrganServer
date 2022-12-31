package colorListener.color

import sound.frequencyBins.FrequencyBin
import java.awt.Color

class FakeColorFactory : ColorFactoryInterface {

    var frequencyBins: List<FrequencyBin>? = null
    val color: Color = Color.orange

    override fun colorFrom(frequencyBins: List<FrequencyBin>): Color {
        this.frequencyBins = frequencyBins
        return color
    }

}