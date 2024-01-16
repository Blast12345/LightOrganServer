package sound.bins.frequency

class FrequencyBinsFactory(
    private val frequencyBinFactory: FrequencyBinFactory = FrequencyBinFactory()
) {

    fun create(magnitudes: DoubleArray, granularity: Float): FrequencyBins {
        val frequencyBins: MutableList<FrequencyBin> = mutableListOf()

        magnitudes.forEachIndexed { index, magnitude ->
            val frequencyBin = frequencyBinFactory.create(index, granularity, magnitude)
            frequencyBins.add(frequencyBin)
        }

        return frequencyBins
    }

}
