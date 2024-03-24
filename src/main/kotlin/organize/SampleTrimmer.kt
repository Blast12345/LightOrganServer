package organize

// TODO:
class SampleTrimmer {

    fun trim(samples: DoubleArray, length: Int): DoubleArray {
        return samples
            .takeLast(length)
            .toDoubleArray()
    }

}
