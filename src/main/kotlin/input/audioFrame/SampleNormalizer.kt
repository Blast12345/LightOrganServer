package input.audioFrame

import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioFormat


// Reference: https://stackoverflow.com/questions/29560491/fourier-transforming-a-byte-array
class SampleNormalizer {

    fun normalize(formatSpecificSamples: ByteArray, format: AudioFormat): DoubleArray {
        val bitDepth = getBitDepth(format)
        val buffer = getBuffer(formatSpecificSamples, format)
        return getNormalizedSamples(bitDepth, buffer)
    }

    private fun getBitDepth(format: AudioFormat): Int {
        return format.sampleSizeInBits
    }

    private fun getBuffer(formatSpecificSamples: ByteArray, format: AudioFormat): ByteBuffer {
        val buffer = ByteBuffer.wrap(formatSpecificSamples)
        val byteOrder = getByteOrder(format)
        buffer.order(byteOrder)
        return buffer
    }

    private fun getByteOrder(format: AudioFormat): ByteOrder {
        return if (format.isBigEndian) {
            ByteOrder.BIG_ENDIAN
        } else {
            ByteOrder.LITTLE_ENDIAN
        }
    }

    private fun getNormalizedSamples(bitDepth: Int, buffer: ByteBuffer): DoubleArray {
        val numberOfSamplesAfterNormalizing = getNumberOfSamplesAfterNormalizing(bitDepth, buffer)
        var normalizedSamples = DoubleArray(numberOfSamplesAfterNormalizing)

        for (i in normalizedSamples.indices) {
            normalizedSamples[i] = getSample(bitDepth, buffer)
        }

        return normalizedSamples
    }

    private fun getNumberOfSamplesAfterNormalizing(bitDepth: Int, buffer: ByteBuffer): Int {
        val bufferSize = buffer.capacity()

        return bufferSize * Byte.SIZE_BITS / bitDepth
    }

    private fun getSample(bitDepth: Int, buffer: ByteBuffer): Double {
        @Suppress("MagicNumber")
        return when (bitDepth) {
            8 -> buffer.get() / Byte.MAX_VALUE.toDouble()
            16 -> buffer.short / Short.MAX_VALUE.toDouble()
            32 -> buffer.int / Int.MAX_VALUE.toDouble()
            64 -> buffer.long / Long.MAX_VALUE.toDouble()
            else -> throw UnsupportedBitDepthException(bitDepth)
        }
    }

}
