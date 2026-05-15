package dsp

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class GainTests {

    private val samples = floatArrayOf(0f, 1f, 2f, 3f)

    private fun createSUT(): Gain {
        return Gain()
    }

    @Test
    fun `a 6 dB gain boost doubles the signals amplitude`() {
        val sut = createSUT()

        val actual = sut.apply(samples, 6f)

        val expected = floatArrayOf(0f, 2f, 4f, 6f)
        assertArrayEquals(expected, actual, 0.1f)
    }

    @Test
    fun `a 6 dB gain reduction halves the signals amplitude`() {
        val sut = createSUT()

        val actual = sut.apply(samples, -6f)
        
        val expected = floatArrayOf(0f, 0.5f, 1f, 1.5f)
        assertArrayEquals(expected, actual, 0.1f)
    }

}