package colorListener.color

import colorListener.sound.frequencyBins.FrequencyBin

class FakeHueFactory : HueFactoryInterface {

    var frequencyBins: List<FrequencyBin>? = null
    var hue: Float? = 0.123f

    override fun hueFrom(frequencyBins: List<FrequencyBin>): Float? {
        this.frequencyBins = frequencyBins
        return hue
    }

}