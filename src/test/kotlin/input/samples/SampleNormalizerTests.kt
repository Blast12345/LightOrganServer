package input.samples

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.byteArray.byteArrayOf
import java.nio.ByteOrder
import javax.sound.sampled.AudioFormat

class SampleNormalizerTests {

    private val normalizedSamples = doubleArrayOf(-1.0, 1.0)

    private fun createSUT(): SampleNormalizer {
        return SampleNormalizer()
    }

    @Test
    fun `normalize 8-bit audio`() {
        val sut = createSUT()
        val samples = byteArrayOf(Byte.MIN_VALUE, Byte.MAX_VALUE)
        val format = AudioFormat(44100F, 8, 1, true, true)
        val actual = sut.normalize(samples, format)
        assertArrayEquals(normalizedSamples, actual, 0.01)
    }

    @Test
    fun `normalize 16-bit audio`() {
        val sut = createSUT()
        val samples = byteArrayOf(Short.MIN_VALUE, Short.MAX_VALUE)
        val format = AudioFormat(44100F, 16, 1, true, true)
        val actual = sut.normalize(samples, format)
        assertArrayEquals(normalizedSamples, actual, 0.01)
    }

    @Test
    fun `normalize 32-bit audio`() {
        val sut = createSUT()
        val samples = byteArrayOf(Int.MIN_VALUE, Int.MAX_VALUE)
        val format = AudioFormat(44100F, 32, 1, true, true)
        val actual = sut.normalize(samples, format)
        assertArrayEquals(normalizedSamples, actual, 0.01)
    }

    @Test
    fun `normalize 64-bit audio`() {
        val sut = createSUT()
        val samples = byteArrayOf(Long.MIN_VALUE, Long.MAX_VALUE)
        val format = AudioFormat(44100F, 64, 1, true, true)
        val actual = sut.normalize(samples, format)
        assertArrayEquals(normalizedSamples, actual, 0.01)
    }

    @Test
    fun `throw an exception for unsupported bit depth`() {
        val sut = createSUT()
        val samples = ByteArray(128)
        val format = AudioFormat(44100F, 128, 1, true, true)
        assertThrows<Exception>(
            executable = { sut.normalize(samples, format) },
            message = "128-bit audio is unsupported."
        )
    }

    @Test
    fun `normalize big endian audio`() {
        val sut = createSUT()
        val samples = byteArrayOf(Short.MIN_VALUE, Short.MAX_VALUE, byteOrder = ByteOrder.BIG_ENDIAN)
        val format = AudioFormat(44100F, 16, 1, true, true)
        val actual = sut.normalize(samples, format)
        assertArrayEquals(normalizedSamples, actual, 0.01)
    }

    @Test
    fun `normalize little endian audio`() {
        val sut = createSUT()
        val samples = byteArrayOf(Short.MIN_VALUE, Short.MAX_VALUE, byteOrder = ByteOrder.LITTLE_ENDIAN)
        val format = AudioFormat(44100F, 16, 1, true, false)
        val actual = sut.normalize(samples, format)
        assertArrayEquals(normalizedSamples, actual, 0.01)
    }

}