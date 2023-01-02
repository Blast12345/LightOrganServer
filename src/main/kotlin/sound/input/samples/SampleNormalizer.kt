package sound.input.samples

import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioFormat

interface SampleNormalizerInterface {
    fun normalize(formatSpecificSamples: ByteArray, format: AudioFormat): DoubleArray
}

class SampleNormalizer : SampleNormalizerInterface {

    // Reference: https://stackoverflow.com/questions/29560491/fourier-transforming-a-byte-array
    override fun normalize(formatSpecificSamples: ByteArray, format: AudioFormat): DoubleArray {
        val bitDepth = getBitDepthFor(format)
        val buffer = getBufferFor(formatSpecificSamples, format)
        return getNormalizedSamplesFor(bitDepth, buffer)
    }

    private fun getBitDepthFor(format: AudioFormat): Int {
        return format.sampleSizeInBits
    }

    private fun getBufferFor(formatSpecificSamples: ByteArray, format: AudioFormat): ByteBuffer {
        val buffer = ByteBuffer.wrap(formatSpecificSamples)
        val byteOrder = getByteOrderFor(format)
        buffer.order(byteOrder)
        return buffer
    }

    private fun getByteOrderFor(format: AudioFormat): ByteOrder {
        return if (format.isBigEndian) {
            ByteOrder.BIG_ENDIAN
        } else {
            ByteOrder.LITTLE_ENDIAN
        }
    }

    private fun getNormalizedSamplesFor(bitDepth: Int, buffer: ByteBuffer): DoubleArray {
        val numberOfSamplesAfterNormalizing = getNumberOfSamplesAfterNormalizingFor(bitDepth, buffer)
        var normalizedSamples = DoubleArray(numberOfSamplesAfterNormalizing)

        for (i in normalizedSamples.indices) {
            normalizedSamples[i] = getSampleFor(bitDepth, buffer)
        }

        return normalizedSamples
    }

    private fun getNumberOfSamplesAfterNormalizingFor(bitDepth: Int, buffer: ByteBuffer): Int {
        return buffer.capacity() * 8 / bitDepth
    }

    private fun getSampleFor(bitDepth: Int, buffer: ByteBuffer): Double {
        return when (bitDepth) {
            8 -> buffer.get() / Byte.MAX_VALUE.toDouble()
            16 -> buffer.short / Short.MAX_VALUE.toDouble()
            32 -> buffer.int / Int.MAX_VALUE.toDouble()
            64 -> buffer.long / Long.MAX_VALUE.toDouble()
            else -> throw Exception("$bitDepth-bit audio is unsupported.")
        }
    }

}