package sound.signalProcessor

typealias Window = DoubleArray

class OverlapAdd {

    // TODO: Test
    fun process(samples: DoubleArray, overlaps: Int, overlapPercent: Float): List<Window> {
        val windows = mutableListOf<DoubleArray>()

        for (i in 0 until overlaps) {
            val window = samples.sliceArray(i until i + samples.size / overlaps)
            windows.add(window)
        }

//        val windowSize = samples.size / overlaps
//        val stepSize = (windowSize * (1 - overlapPercent)).toInt()
//
//        var start = 0
//        while (start + windowSize <= samples.size) {
//            val window = samples.sliceArray(start until start + windowSize)
//            windows.add(window)
//            start += stepSize
//        }

        return windows
    }

}
