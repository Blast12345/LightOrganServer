package lightOrgan.sound.signalProcessing.hannFilter

class NormalizedHannFilter(
    private val hannFilter: HannFilterInterface = HannFilter(),
    private val hannFilterNormalizer: HannFilterNormalizerInterface = HannFilterNormalizer(),
) : HannFilterInterface {

    override fun filter(signal: DoubleArray): DoubleArray {
        val filteredSamples = hannFilter.filter(signal)
        return hannFilterNormalizer.normalize(filteredSamples)
    }

}