package sound.signalProcessor.filters.window

// Reference: https://community.sw.siemens.com/s/article/window-correction-factors
class HannFilterCorrector {

    private val correctionFactor = 2.0

    fun correct(windowedSignal: DoubleArray): DoubleArray {
        val correctedSignal = windowedSignal.map { it * correctionFactor }
        return correctedSignal.toDoubleArray()
    }

}