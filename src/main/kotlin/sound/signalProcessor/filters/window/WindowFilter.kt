package sound.signalProcessor.filters.window

interface WindowFilter {
    fun applyTo(samples: DoubleArray): DoubleArray
}
