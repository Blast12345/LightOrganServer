package sound.signalProcessor.filters.window

interface WindowFilter {
    fun applyTo(signal: DoubleArray): DoubleArray
}