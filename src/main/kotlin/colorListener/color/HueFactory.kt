package colorListener.color

import colorListener.sound.FrequencyBin
import colorListener.sound.averageFrequency
import colorListener.sound.maximumFrequency
import colorListener.sound.minimumFrequency

interface HueFactoryInterface {
    fun hueFrom(frequencyBins: List<FrequencyBin>): Float
}

class HueFactory : HueFactoryInterface {

    override fun hueFrom(frequencyBins: List<FrequencyBin>): Float {
        val averageFrequency = frequencyBins.averageFrequency()
        val minimumFrequency = frequencyBins.minimumFrequency()
        val maximumFrequency = frequencyBins.maximumFrequency()
        val hue = (averageFrequency - minimumFrequency) / maximumFrequency
        return hue.toFloat()
    }

}