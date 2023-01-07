package sound.signalProcessing.hannFilter

interface HannFilterNormalizerInterface {
    fun normalize(windowedSignal: DoubleArray): DoubleArray
}

// Reference: https://community.sw.siemens.com/s/article/window-correction-factors
class HannFilterNormalizer() : HannFilterNormalizerInterface {

    private val correctionFactor = 2.0

    override fun normalize(windowedSignal: DoubleArray): DoubleArray {
        val correctedSignal = windowedSignal.map { it * correctionFactor }
        return correctedSignal.toDoubleArray()
    }

}