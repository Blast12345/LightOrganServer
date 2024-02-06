package sound.signalProcessor

typealias Window = DoubleArray

class OverlapAdd {

    // TODO: Test
    fun process(samples: DoubleArray, overlaps: Int, overlapPercent: Float): List<Window> {
        val windowSize = samples.size / overlaps
        val overlapSize = (windowSize * overlapPercent).toInt()
        val windows = mutableListOf<DoubleArray>()

        var start = 0
        while (start < samples.size) {
            val window = DoubleArray(windowSize) { if (it + start < samples.size) samples[it + start] else 0.0 }
            windows.add(window)
            start += overlapSize
            if (windows.size >= overlaps) break
        }

        return windows
    }

    fun overlapAdd2(signal: DoubleArray, numOverlaps: Int, overlapPercentage: Float): List<DoubleArray> {
        val windows = mutableListOf<DoubleArray>()
        val windowSize = (signal.size / (1 + (numOverlaps - 1) * (1 - overlapPercentage))).toInt()
        val stepSize = (windowSize * (1 - overlapPercentage)).toInt()

        for (i in 0 until numOverlaps) {
            val start = i * stepSize
            if (start + windowSize <= signal.size) {
                val window = signal.sliceArray(start until start + windowSize)
                windows.add(window)
            }
        }

        return windows
    }

}
