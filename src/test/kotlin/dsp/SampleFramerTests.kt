package dsp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SampleFramerTests {

    private val samples = floatArrayOf(1f, 2f, 3f, 4f, 5f)

    @Test
    fun `frame the samples by taking the N latest samples`() {
        val sut = SampleFramer(2)

        val frame = sut.frame(samples)

        val expected = floatArrayOf(4f, 5f)
        Assertions.assertArrayEquals(expected, frame)
    }

    @Test
    fun `frame the samples with an offset`() {
        val sut = SampleFramer(2)

        val frame = sut.frame(samples, offset = 1)

        val expected = floatArrayOf(3f, 4f)
        Assertions.assertArrayEquals(expected, frame)
    }

    @Test
    fun `throw when the frame size exceeds the available samples`() {
        val sut = SampleFramer(samples.size + 1)

        assertThrows<InsufficientSamplesException> { sut.frame(samples) }
    }

    @Test
    fun `throw when the offset exceeds the available samples`() {
        val sut = SampleFramer(samples.size)

        assertThrows<InsufficientSamplesException> { sut.frame(samples, offset = 1) }
    }

}