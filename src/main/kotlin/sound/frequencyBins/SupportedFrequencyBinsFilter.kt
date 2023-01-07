package sound.frequencyBins

interface SupportedFrequencyBinsFilterInterface {
    fun filter(frequencyBins: FrequencyBinList, lowestFrequency: Float): FrequencyBinList
}

class SupportedFrequencyBinsFilter : SupportedFrequencyBinsFilterInterface {

    override fun filter(frequencyBins: FrequencyBinList, lowestFrequency: Float): FrequencyBinList {
        return frequencyBins.filter { it.frequency >= lowestFrequency }
    }

}