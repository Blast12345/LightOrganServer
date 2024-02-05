package sound.signalProcessor

class Downsampler {

    // TODO: Test
    fun decimate(samples: DoubleArray, factor: Int): DoubleArray {
        return samples.filterIndexed { index, _ -> index % factor == 0 }.toDoubleArray()
    }
}
