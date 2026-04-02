package dsp.windowing

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class WindowTypeTests {

    @Test
    fun `Hann produces expected coefficients`() {
        val sut = WindowType.Hann.createWindow()

        val result = sut.coefficients(5)

        val expected = floatArrayOf(0.0f, 0.5f, 1.0f, 0.5f, 0.0f)
        assertArrayEquals(expected, result, 0.001f)
    }

    @Test
    fun `BlackmanHarris3Term produces expected coefficients`() {
        val sut = WindowType.BlackmanHarris3Term.createWindow()

        val result = sut.coefficients(5)

        val expected = floatArrayOf(0.00532f, 0.34610f, 1.0f, 0.34610f, 0.00532f)
        assertArrayEquals(expected, result, 0.001f)
    }

    @Test
    fun `BlackmanHarris4Term produces expected coefficients`() {
        val sut = WindowType.BlackmanHarris4Term.createWindow()

        val result = sut.coefficients(5)

        val expected = floatArrayOf(0.00006f, 0.21747f, 1.0f, 0.21747f, 0.00006f)
        assertArrayEquals(expected, result, 0.001f)
    }

}