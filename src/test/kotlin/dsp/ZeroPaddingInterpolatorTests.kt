package dsp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ZeroPaddingInterpolatorTests {

    private val frame = floatArrayOf(1f, 2f, 3f)

    @Test
    fun `pad the frame with zeros until the desired size is reached`() {
        val sut = ZeroPaddingInterpolator(desiredSize = 6)

        val result = sut.interpolate(frame)

        Assertions.assertEquals(6, result.size)
        Assertions.assertEquals(0f, result[3])
        Assertions.assertEquals(0f, result[4])
        Assertions.assertEquals(0f, result[5])
    }

    @Test
    fun `scales original samples to preserve magnitude`() {
        val sut = ZeroPaddingInterpolator(desiredSize = 6)
        val frame = floatArrayOf(1f, 2f, 3f)

        val result = sut.interpolate(frame)

        Assertions.assertEquals(2f, result[0], 0.001f)
        Assertions.assertEquals(4f, result[1], 0.001f)
        Assertions.assertEquals(6f, result[2], 0.001f)
    }

    @Test
    fun `throws when desired size is smaller than frame size`() {
        val sut = ZeroPaddingInterpolator(desiredSize = 2)

        assertThrows<InvalidInterpolationSizeException> { sut.interpolate(frame) }
    }

}