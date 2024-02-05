package sound.signalProcessor

class AudioTrimmer {

    fun trim(samples: DoubleArray, length: Int): DoubleArray {
        return samples
            .takeLast(length)
            .toDoubleArray()
    }

}
