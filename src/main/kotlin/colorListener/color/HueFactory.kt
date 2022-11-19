package colorListener.color

import colorListener.sound.frequencyBins.FrequencyBin
import colorListener.sound.frequencyBins.averageFrequency
import colorListener.sound.frequencyBins.maximumFrequency
import colorListener.sound.frequencyBins.minimumFrequency

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