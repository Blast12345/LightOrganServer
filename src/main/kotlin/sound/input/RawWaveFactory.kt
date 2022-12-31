package sound.input

import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.UnsupportedAudioFileException
import kotlin.math.pow

interface RawWaveFactoryInterface {
    fun rawWaveFrom(data: ByteArray, format: AudioFormat): DoubleArray
}

// TODO: What are we actually doing in this class? The name may be misleading.
// TODO: Test me
class RawWaveFactory : RawWaveFactoryInterface {

    // Reference: https://stackoverflow.com/questions/29560491/fourier-transforming-a-byte-array
    override fun rawWaveFrom(data: ByteArray, format: AudioFormat): DoubleArray {
        // Audio Info
        val bits = format.sampleSizeInBits
        val max = 2.0.pow(bits.toDouble() - 1)

        // Buffer
        val byteOrder = if (format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN
        val buffer = ByteBuffer.wrap(data)
        buffer.order(byteOrder)

        // Samples
        var samples = DoubleArray(data.size * 8 / bits)

        for (i in samples.indices) {
            when (bits) {
                8 -> samples[i] = buffer.get() / max
                16 -> samples[i] = buffer.short / max
                32 -> samples[i] = buffer.int / max
                64 -> samples[i] = buffer.long / max
                else -> throw UnsupportedAudioFileException()
            }
        }

        return samples
    }

}