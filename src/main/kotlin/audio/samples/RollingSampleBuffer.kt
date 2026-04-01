package audio.samples

import extensions.takeLastArray
import logging.Logger

class RollingSampleBuffer(initialSize: Int = 0) {

    private var samples: FloatArray = FloatArray(initialSize)

    val current: FloatArray
        get() = samples.copyOf()

    var size: Int = samples.size
        set(value) {
            if (field == value) return
            field = value
            resize(value)
        }

    private fun resize(newSize: Int) {
        val old = samples
        samples = FloatArray(newSize)
        val copyCount = minOf(old.size, newSize)
        old.copyInto(samples, destinationOffset = newSize - copyCount, startIndex = old.size - copyCount)
    }

    fun append(newSamples: FloatArray): FloatArray {
        checkForDiscontinuity(newSamples)

        val trimmed = newSamples.takeLastArray(samples.size)
        samples.copyInto(samples, destinationOffset = 0, startIndex = trimmed.size)
        trimmed.copyInto(samples, destinationOffset = samples.size - trimmed.size)

        return current
    }

    private fun checkForDiscontinuity(newSamples: FloatArray) {
        if (newSamples.size > samples.size) {
            Logger.warning("Rolling buffer has dropped samples. (${newSamples.size} new samples, buffer capacity ${samples.size})")
        }
    }

    fun reset() {
        samples.fill(0f)
    }

}