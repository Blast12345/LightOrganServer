package input.audioInput

// TODO: Test me
class SampleBuffer(size: Int) {

    private val samples = FloatArray(size)

    val current: FloatArray
        get() = samples.copyOf()

    fun append(newSamples: FloatArray) {
        samples.copyInto(samples, destinationOffset = 0, startIndex = newSamples.size)
        newSamples.copyInto(samples, destinationOffset = samples.size - newSamples.size)
    }

}