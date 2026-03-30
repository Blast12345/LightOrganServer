package audio.samples

import extensions.takeLastArray
import logging.Logger

class RollingSampleBuffer {

    private var samples: FloatArray = FloatArray(0)

    val current: FloatArray
        get() = samples.copyOf()

    fun append(newSamples: FloatArray, requiredSize: Int): FloatArray {
        resizeIfNeeded(requiredSize)
        checkForDiscontinuity(newSamples)

        val trimmed = newSamples.takeLastArray(samples.size)
        samples.copyInto(samples, destinationOffset = 0, startIndex = trimmed.size)
        trimmed.copyInto(samples, destinationOffset = samples.size - trimmed.size)

        return current
    }

    private fun resizeIfNeeded(requiredSize: Int) {
        if (requiredSize == samples.size) return

        val old = samples
        samples = FloatArray(requiredSize)
        val copyCount = minOf(old.size, requiredSize)
        old.copyInto(samples, destinationOffset = requiredSize - copyCount, startIndex = old.size - copyCount)
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