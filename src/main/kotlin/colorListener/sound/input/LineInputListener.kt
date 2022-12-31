package colorListener.sound.input

import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.*
import kotlin.math.pow

// TODO: Should sample rate be float? Are there any downsides?
typealias NextAudioSample = (sampleRate: Int, sampleSize: Int, signal: DoubleArray) -> Unit

interface LineInputListenerInterface {
    fun listenForNextAudioSample(lambda: NextAudioSample)
}

class LineInputListener : LineInputListenerInterface {

    override fun listenForNextAudioSample(lambda: NextAudioSample) {
        val input = getInput() as TargetDataLine

//        val sampleSize = 4096
//        val bufferSize = sampleSize * input.format.frameSize
//        var buffer = ByteArray(bufferSize)
//        input.open(input.format, bufferSize)

        // NOTE: I think frame size is dependent on bit-depth. 8-bit audio would like be 1, 16 would be 2, 24 would be 3, and 32 would be 4
        val sampleSize = input.bufferSize / input.format.frameSize
        input.open()


        input.start() // Is this needed?


        var buffer = ByteArray(input.bufferSize)
        while (true) {
            // TODO: New data becomes available every ~10 ms. I don't know how this delay is derived.
            if (input.available() <= 0) {
                continue
            }

            val newData = ByteArray(input.available())
//            input.read(newData, 0, newData.size)
//            val rawWave = doubleArrayFrom(newData, input.format)

            val readBytes = input.read(newData, 0, newData.size)
            val rolloverData = buffer.drop(readBytes).toByteArray()
            buffer = rolloverData + newData
            val rawWave = doubleArrayFrom(buffer, input.format)

            // TODO: Something here isn't quite right; the frequency bins don't line up with reality. They are about 2x off.
            // TODO: I don't think I need the sample size once I have the raw wave
            lambda(input.format.sampleRate.toInt(), 0, rawWave)
        }
    }

    // Reference: https://stackoverflow.com/questions/29560491/fourier-transforming-a-byte-array
    private fun doubleArrayFrom(data: ByteArray, format: AudioFormat): DoubleArray {
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

    // MARK: DefaultInputUtility
    private fun getInput(): Line {
        val inputs = getInputs()
        return inputs.first() // NOTE: This could be extended in the future to take a searchable string
    }

    // MARK: LineInputUtility
    private fun getInputs(): List<Line> {
        var inputs: MutableList<Line> = mutableListOf()

        for (audioDevice in getAudioDevices()) {
            val targetLineInfo = audioDevice.targetLineInfo
            val targetDataLineInfo = targetLineInfo.filter { it.lineClass == TargetDataLine::class.java }
            val targetDataLines = targetDataLineInfo.map { audioDevice.getLine(it) }
            inputs.addAll(targetDataLines)
        }

        return inputs
    }

    // MARK: AudioDeviceUtility?
    private fun getAudioDevices(): List<Mixer> {
        return getMixerInfo().map { AudioSystem.getMixer(it) }
    }

    private fun getMixerInfo(): Array<Mixer.Info> {
        return AudioSystem.getMixerInfo()
    }

}