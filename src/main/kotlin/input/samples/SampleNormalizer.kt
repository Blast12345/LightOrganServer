package input.samples

import java.nio.ByteBuffer
import java.nio.ByteOrder

// Reference: https://stackoverflow.com/questions/29560491/fourier-transforming-a-byte-array
class SampleNormalizer(
    private val bitDepth: Int,
    private val byteOrder: ByteOrder
) {

    fun normalize(frameData: ByteArray): FloatArray {
        val buffer = getBuffer(frameData, byteOrder)
        return getNormalizedSamples(bitDepth, buffer)
    }

    private fun getBuffer(formatSpecificSamples: ByteArray, byteOrder: ByteOrder): ByteBuffer {
        val buffer = ByteBuffer.wrap(formatSpecificSamples)
        buffer.order(byteOrder)
        return buffer
    }

    private fun getNormalizedSamples(bitDepth: Int, buffer: ByteBuffer): FloatArray {
        val numberOfSamplesAfterNormalizing = getNumberOfSamplesAfterNormalizing(bitDepth, buffer)
        val normalizedSamples = FloatArray(numberOfSamplesAfterNormalizing)

        for (i in normalizedSamples.indices) {
            normalizedSamples[i] = getSample(bitDepth, buffer)
        }

        return normalizedSamples
    }

    private fun getNumberOfSamplesAfterNormalizing(bitDepth: Int, buffer: ByteBuffer): Int {
        val bufferSize = buffer.capacity()

        return bufferSize * Byte.SIZE_BITS / bitDepth
    }

    private fun getSample(bitDepth: Int, buffer: ByteBuffer): Float {
        @Suppress("MagicNumber")
        return when (bitDepth) {
            8 -> buffer.get() / Byte.MAX_VALUE.toFloat()
            16 -> buffer.short / Short.MAX_VALUE.toFloat()
            32 -> buffer.int / Int.MAX_VALUE.toFloat()
            64 -> buffer.long / Long.MAX_VALUE.toFloat()
            else -> throw UnsupportedBitDepthException(bitDepth)
        }
    }

}