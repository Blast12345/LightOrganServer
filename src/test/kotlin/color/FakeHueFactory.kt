package color

import sound.frequencyBins.FrequencyBin

class FakeHueFactory : HueFactoryInterface {

    var frequencyBins: List<FrequencyBin>? = null
    var hue: Float? = 0.123f

    override fun createFrom(frequencyBins: List<FrequencyBin>): Float? {
        this.frequencyBins = frequencyBins
        return hue
    }

}