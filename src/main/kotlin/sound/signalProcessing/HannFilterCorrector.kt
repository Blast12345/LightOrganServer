package sound.signalProcessing

interface HannFilterCorrectorInterface {
    fun correct(windowedSignal: DoubleArray): DoubleArray
}

// Reference: https://community.sw.siemens.com/s/article/window-correction-factors
class HannFilterCorrector() : HannFilterCorrectorInterface {

    private val correctionFactor = 2.0

    override fun correct(windowedSignal: DoubleArray): DoubleArray {
        val correctedSignal = windowedSignal.map { it * correctionFactor }
        return correctedSignal.toDoubleArray()
    }

}