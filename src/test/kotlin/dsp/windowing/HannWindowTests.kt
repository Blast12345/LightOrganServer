package dsp.windowing

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HannWindowTests {

    private val frame = FloatArray(5) { 1f }

    @Test
    fun `edge samples are zero`() {
        val sut = HannWindow()

        val result = sut.appliedTo(frame)

        assertEquals(0f, result.first())
        assertEquals(0f, result.last())
    }

    @Test
    fun `center sample is unchanged`() {
        val sut = HannWindow()

        val result = sut.appliedTo(frame)

        assertEquals(1f, result[2], 0.001f)
    }

    @Test
    fun `output is symmetric`() {
        val sut = HannWindow()
        val frame = FloatArray(5) { 1f }

        val result = sut.appliedTo(frame)

        assertEquals(result[0], result[4])
        assertEquals(result[1], result[3])
    }

    @Test
    fun `get the amplitude correction factor`() {
        val sut = HannWindow()

        assertEquals(2f, sut.amplitudeCorrectionFactor)
    }

    @Test
    fun `get the energy correction factor`() {
        val sut = HannWindow()

        assertEquals(1.63f, sut.energyCorrectionFactor)
    }

    @Test
    fun `verify known output`() {
        val sut = HannWindow()

        val result = sut.appliedTo(frame)

        val expected = floatArrayOf(0.0f, 0.5f, 1.0f, 0.5f, 0.0f)
        assertArrayEquals(expected, result, 0.001f)
    }

}
