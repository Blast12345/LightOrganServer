package dsp.window

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HannWindowTests {

    // This is inherently stateless, so no need to create a new instance for each test.
    private val sut = HannWindow()
    private val frame = FloatArray(5) { 1f }

    @Test
    fun `edge samples are zero`() {
        val result = sut.appliedTo(frame)

        assertEquals(0f, result.first())
        assertEquals(0f, result.last())
    }

    @Test
    fun `center sample is unchanged`() {
        val result = sut.appliedTo(frame)

        assertEquals(1f, result[2], 0.001f)
    }

    @Test
    fun `output is symmetric`() {
        val frame = FloatArray(5) { 1f }

        val result = sut.appliedTo(frame)

        assertEquals(result[0], result[4])
        assertEquals(result[1], result[3])
    }

    @Test
    fun `get the amplitude correction factor`() {
        assertEquals(2f, sut.amplitudeCorrectionFactor)
    }

    @Test
    fun `get the energy correction factor`() {
        assertEquals(1.63f, sut.energyCorrectionFactor)
    }

    @Test
    fun `verify known output`() {
        val result = sut.appliedTo(frame)

        val expected = floatArrayOf(0.0f, 0.5f, 1.0f, 0.5f, 0.0f)
        assertArrayEquals(expected, result, 0.001f)
    }

}
