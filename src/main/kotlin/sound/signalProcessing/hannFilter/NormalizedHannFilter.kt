package sound.signalProcessing.hannFilter

class NormalizedHannFilter(
    private val standardHannFilter: StandardHannFilter = StandardHannFilter(),
    private val hannFilterNormalizer: HannFilterNormalizer = HannFilterNormalizer(),
) : HannFilter {

    override fun filter(signal: DoubleArray): DoubleArray {
        val filteredSamples = standardHannFilter.filter(signal)
        return hannFilterNormalizer.normalize(filteredSamples)
    }

}