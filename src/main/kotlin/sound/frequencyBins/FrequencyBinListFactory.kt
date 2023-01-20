package sound.frequencyBins

import config.Config

interface FrequencyBinListFactoryInterface {
    fun create(magnitudes: DoubleArray, granularity: Float): FrequencyBinList
}

class FrequencyBinListFactory(
    config: Config,
    private val frequencyBinFactory: FrequencyBinFactoryInterface = FrequencyBinFactory(config)
) : FrequencyBinListFactoryInterface {

    override fun create(magnitudes: DoubleArray, granularity: Float): FrequencyBinList {
        val frequencyBins: MutableList<FrequencyBin> = mutableListOf()

        magnitudes.forEachIndexed { index, magnitude ->
            val frequencyBin = frequencyBinFactory.create(index, granularity, magnitude)
            frequencyBins.add(frequencyBin)
        }

        return frequencyBins
    }

}