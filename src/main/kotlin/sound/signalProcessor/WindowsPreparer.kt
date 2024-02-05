package sound.signalProcessor

class WindowsPreparer(
    private val windowPreparer: SignalProcessor = SignalProcessor()
) {

    // TODO: Test
    fun prepare(windows: List<DoubleArray>): List<DoubleArray> {
        return windows.map { windowPreparer.process(it) }
    }

}
