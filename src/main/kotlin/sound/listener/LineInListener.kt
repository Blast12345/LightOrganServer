package sound.listener

import com.sun.org.apache.xpath.internal.operations.Bool
import sound.LineIn
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine
import javax.sound.sampled.UnsupportedAudioFileException
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

class LineInListener(private val lineIn: LineIn): Listener {

    private var isListening = false
    private var waveFrame = doubleArrayOf()
    private var buffer = ByteArray(lineIn.bufferSize())

    fun startListening() {
        isListening = true

        while (isListening) {
            updateWaveFrameIfAble()
        }
    }

    private fun updateWaveFrameIfAble() {
        if (lineHasData()) {
            updateWaveFrameForNewData()
        }
    }

    private fun lineHasData(): Boolean {
        return lineIn.hasDataAvailable()
    }

    private fun updateWaveFrameForNewData() {
        waveFrame = getNextWaveFrame()
    }

    private fun getNextWaveFrame(): DoubleArray {
        val rawWaveFrame = getNextRawWaveFrame()
        return applyHannWindowFilter(rawWaveFrame)
    }

    private fun getNextRawWaveFrame(): DoubleArray {
        val byteWaveFrame = getNextByteWaveFrame()
        return convertByteWaveToDoubleWave(byteWaveFrame) // TODO: Be more specific about what is happening
    }

    private fun getNextByteWaveFrame(): ByteArray {
        // We gather data into a rolling buffer (First-In-First-Out)
        // E.g. Buffer size of 4096 updating in increments of 1024 means we can update the rolling buffer 4 times
        // in the same time it would take to get a full 4096 samples
        val availableBytes = lineIn.availableBytes()
        val newData = lineIn.readAvailable()
        val rolloverData = buffer.drop(availableBytes).toByteArray()
        return rolloverData + newData
    }

    // Reference: https://stackoverflow.com/questions/29560491/fourier-transforming-a-byte-array
    private fun convertByteWaveToDoubleWave(data: ByteArray): DoubleArray {
        // Audio Info
        val bitDepth = lineIn.bitDepth()
        val max = 2.0.pow(bitDepth.toDouble() - 1)

        // Buffer
        val byteOrder = if (lineIn.isBigEndian()) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN
        val buffer = ByteBuffer.wrap(data)
        buffer.order(byteOrder)

        // Samples
        var samples = DoubleArray(data.size * 8 / bitDepth)

        for (i in samples.indices) {
            when (bitDepth) {
                8 -> samples[i] = buffer.get() / max
                16 -> samples[i] = buffer.short / max
                32 -> samples[i] = buffer.int / max
                64 -> samples[i] = buffer.long / max
                else -> throw UnsupportedAudioFileException()
            }
        }

        return samples
    }

    // apply a window algorithm to reduce spectral leakage
    // Reference: https://dsp.stackexchange.com/questions/19776/is-it-necessary-to-apply-some-window-method-to-obtain-the-fft-java
    private fun applyHannWindowFilter(data: DoubleArray): DoubleArray {
        val output = DoubleArray(data.size)

        for (i in data.indices) {
            val multiplier = 0.5 * (1 - cos(2 * PI * i / (data.size - 1)))
            output[i] = multiplier * data[i]
        }

        return output
    }

    fun stopListening() {
        isListening = false
    }

    override fun getSampleRate(): Double {
        return lineIn.getSampleRate()
    }

    override fun getSampleSize(): Int {
        return lineIn.getSampleSize()
    }

    override fun getFftData(): DoubleArray {
        return waveFrame
    }

}