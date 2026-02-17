package audio.audioInput

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.byteArray.byteArrayOf
import java.nio.ByteOrder

class SampleNormalizerTests {

    private val normalizedSamples = floatArrayOf(-1.0f, 1.0f)


    @Test
    fun `normalize 8-bit audio`() {
        val sut = SampleNormalizer(8, ByteOrder.BIG_ENDIAN)
        val samples = byteArrayOf(Byte.MIN_VALUE, Byte.MAX_VALUE)

        val actual = sut.normalize(samples)

        assertArrayEquals(normalizedSamples, actual, 0.01f)
    }

    @Test
    fun `normalize 16-bit audio`() {
        val sut = SampleNormalizer(16, ByteOrder.BIG_ENDIAN)
        val samples = byteArrayOf(Short.MIN_VALUE, Short.MAX_VALUE)

        val actual = sut.normalize(samples)

        assertArrayEquals(normalizedSamples, actual, 0.01f)
    }

    @Test
    fun `normalize 32-bit audio`() {
        val sut = SampleNormalizer(32, ByteOrder.BIG_ENDIAN)
        val samples = byteArrayOf(Int.MIN_VALUE, Int.MAX_VALUE)

        val actual = sut.normalize(samples)

        assertArrayEquals(normalizedSamples, actual, 0.01f)
    }

    @Test
    fun `normalize 64-bit audio`() {
        val sut = SampleNormalizer(64, ByteOrder.BIG_ENDIAN)
        val samples = byteArrayOf(Long.MIN_VALUE, Long.MAX_VALUE)

        val actual = sut.normalize(samples)

        assertArrayEquals(normalizedSamples, actual, 0.01f)
    }

    @Test
    fun `throw an exception for unsupported bit depth`() {
        val sut = SampleNormalizer(128, ByteOrder.BIG_ENDIAN)

        val samples = ByteArray(128)

        assertThrows<Exception>(
            executable = { sut.normalize(samples) },
            message = "128-bit audio is unsupported."
        )
    }

    @Test
    fun `normalize big endian audio`() {
        val sut = SampleNormalizer(16, ByteOrder.BIG_ENDIAN)
        val samples = byteArrayOf(Short.MIN_VALUE, Short.MAX_VALUE, byteOrder = ByteOrder.BIG_ENDIAN)

        val actual = sut.normalize(samples)

        assertArrayEquals(normalizedSamples, actual, 0.01f)
    }

    @Test
    fun `normalize little endian audio`() {
        val sut = SampleNormalizer(16, ByteOrder.LITTLE_ENDIAN)
        val samples = byteArrayOf(Short.MIN_VALUE, Short.MAX_VALUE, byteOrder = ByteOrder.LITTLE_ENDIAN)

        val actual = sut.normalize(samples)

        assertArrayEquals(normalizedSamples, actual, 0.01f)
    }

}