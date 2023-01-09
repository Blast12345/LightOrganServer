package color

import sound.frequencyBins.FrequencyBin

interface HueFactoryInterface {
    fun create(
        dominantBin: FrequencyBin,
        lowestFrequencyBin: FrequencyBin,
        highestFrequencyBin: FrequencyBin
    ): Float
}

class HueFactory() : HueFactoryInterface {

    override fun create(
        dominantBin: FrequencyBin,
        lowestFrequencyBin: FrequencyBin,
        highestFrequencyBin: FrequencyBin
    ): Float {
        return getHue(
            dominantFrequency = dominantBin.frequency,
            lowestFrequency = lowestFrequencyBin.frequency,
            highestFrequency = highestFrequencyBin.frequency
        )
    }

    private fun getHue(
        dominantFrequency: Float,
        lowestFrequency: Float,
        highestFrequency: Float
    ): Float {
        val frequencyRange = highestFrequency - lowestFrequency
        val positionInRange = dominantFrequency - lowestFrequency
        return positionInRange / frequencyRange

    }

}