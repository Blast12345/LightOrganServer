package colorListener.color

import colorListener.sound.FrequencyBin

class FakeHueFactory : HueFactoryInterface {

    var frequencyBins: List<FrequencyBin>? = null
    val hue = 0.123f

    override fun hueFrom(frequencyBins: List<FrequencyBin>): Float {
        this.frequencyBins = frequencyBins
        return hue
    }

}