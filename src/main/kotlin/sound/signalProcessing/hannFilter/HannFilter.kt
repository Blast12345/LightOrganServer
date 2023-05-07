package sound.signalProcessing.hannFilter

interface HannFilter {
    fun filter(signal: DoubleArray): DoubleArray
}