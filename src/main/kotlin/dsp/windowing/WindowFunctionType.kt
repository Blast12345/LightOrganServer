package dsp.windowing

enum class WindowFunctionType {
    Hann,
    BlackmanHarris;

    fun createWindow(): WindowFunction = when (this) {
        Hann -> HannWindow()
        BlackmanHarris -> BlackmanHarrisWindow()
    }
}