package dsp.windowing

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BlackmanHarrisWindowTests {

    private val frame = FloatArray(5) { 1f }

    @Test
    fun `edge samples are near zero`() {
        val sut = BlackmanHarrisWindow()

        val result = sut.appliedTo(frame)

        assertEquals(0f, result.first(), 0.01f)
        assertEquals(0f, result.last(), 0.01f)
    }

    @Test
    fun `center sample is unchanged`() {
        val sut = BlackmanHarrisWindow()

        val result = sut.appliedTo(frame)

        assertEquals(1f, result[2], 0.001f)
    }

    @Test
    fun `output is symmetric`() {
        val sut = BlackmanHarrisWindow()

        val result = sut.appliedTo(frame)

        assertEquals(result[0], result[4], 0.01f)
        assertEquals(result[1], result[3], 0.01f)
    }

    @Test
    fun `coefficients match expected shape`() {
        val sut = BlackmanHarrisWindow()

        val result = sut.coefficients(5)

        val expected = floatArrayOf(0.00006f, 0.21747f, 1.0f, 0.21747f, 0.00006f)
        assertArrayEquals(expected, result, 0.01f)
    }

    @Test
    fun `magnitude correction factor accounts for window size`() {
        val sut = BlackmanHarrisWindow()

        val smallWindow = sut.magnitudeCorrectionFactor(5)
        val largeWindow = sut.magnitudeCorrectionFactor(512)

        assertEquals(3.484f, smallWindow, 0.01f)
        assertEquals(2.788f, largeWindow, 0.01f)
    }

    @Test
    fun `energy correction factor accounts for window size`() {
        val sut = BlackmanHarrisWindow()

        val smallWindow = sut.energyCorrectionFactor(5)
        val largeWindow = sut.energyCorrectionFactor(512)

        assertEquals(2.137f, smallWindow, 0.01f)
        assertEquals(1.970f, largeWindow, 0.01f)
    }

}