package audio.samples

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextPositiveInt

class RollingSampleBufferTests {

    @Test
    fun `initialize an empty buffer`() {
        val size = nextPositiveInt()
        val sut = RollingSampleBuffer(size)

        val expected = FloatArray(size) { 0f }
        assertArrayEquals(expected, sut.current)
    }

    @Test
    fun `protect internal state from being directly modified`() {
        val sut = RollingSampleBuffer(4)
        val original = floatArrayOf(1f, 2f, 3f, 4f)
        sut.append(original)

        val snapshot = sut.current
        snapshot[0] = 999f

        assertArrayEquals(original, sut.current)
    }

    @Test
    fun `appended samples are added to the end of the buffer and the oldest are discarded`() {
        val sut = RollingSampleBuffer(4)

        sut.append(floatArrayOf(1f, 2f))
        sut.append(floatArrayOf(3f, 4f))
        sut.append(floatArrayOf(5f, 6f))

        assertArrayEquals(floatArrayOf(3f, 4f, 5f, 6f), sut.current)
    }

    @Test
    fun `when appending data longer than the buffer can hold, then keep the latest samples`() {
        val sut = RollingSampleBuffer(4)

        sut.append(floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f))

        assertArrayEquals(floatArrayOf(3f, 4f, 5f, 6f), sut.current)
    }

}