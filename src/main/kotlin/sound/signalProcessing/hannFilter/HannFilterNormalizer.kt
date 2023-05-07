package sound.signalProcessing.hannFilter

// Reference: https://community.sw.siemens.com/s/article/window-correction-factors
class HannFilterNormalizer {

    private val correctionFactor = 2.0

    fun normalize(windowedSignal: DoubleArray): DoubleArray {
        val correctedSignal = windowedSignal.map { it * correctionFactor }
        return correctedSignal.toDoubleArray()
    }

}