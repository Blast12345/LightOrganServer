package audio.samples

import extensions.takeLastArray

// TODO: Verify test names
class RollingSampleBuffer(size: Int) {

    private val samples = FloatArray(size)

    val current: FloatArray
        get() = samples.copyOf()

    fun append(newSamples: FloatArray) {
        // Trim our new samples to prevent buffer overflow
        val trimmed = newSamples.takeLastArray(samples.size)

        // Shift existing samples over (i.e., back in time)
        samples.copyInto(samples, destinationOffset = 0, startIndex = trimmed.size)

        // Copy new samples into the end of the buffer (i.e., the most recent time)
        trimmed.copyInto(samples, destinationOffset = samples.size - trimmed.size)
    }

    fun reset() {
        samples.fill(0f)
    }

}