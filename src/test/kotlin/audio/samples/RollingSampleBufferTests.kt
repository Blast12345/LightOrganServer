package audio.samples

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextPositiveInt

class RollingSampleBufferTests {

    private val size = nextPositiveInt()

    // Init
    @Test
    fun `initialize an empty buffer`() {
        val sut = RollingSampleBuffer(size)

        val expected = FloatArray(size) { 0f }
        assertArrayEquals(expected, sut.current)
    }


    // Append
    @Test
    fun `append adds samples to the end of the buffer`() {
        val sut = RollingSampleBuffer(4)

        sut.append(floatArrayOf(1f, 2f))

        val expected = floatArrayOf(0f, 0f, 1f, 2f)
        assertArrayEquals(expected, sut.current)
    }

    @Test
    fun `appending more than the capacity keeps the latest samples`() {
        val sut = RollingSampleBuffer(4)

        sut.append(floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f))

        val expected = floatArrayOf(3f, 4f, 5f, 6f)
        assertArrayEquals(expected, sut.current)
    }

    @Test
    fun `append returns the current buffer state`() {
        val sut = RollingSampleBuffer(4)

        val result = sut.append(floatArrayOf(1f, 2f))

        val expected = floatArrayOf(0f, 0f, 1f, 2f)
        assertArrayEquals(expected, result)
    }

    // Size
    @Test
    fun `get the buffer size`() {
        val sut = RollingSampleBuffer(size)

        assertEquals(size, sut.size)
    }

    @Test
    fun `set the buffer size`() {
        val sut = RollingSampleBuffer()
        val newSize = nextPositiveInt()

        sut.size = newSize

        assertEquals(newSize, sut.size)
    }

    @Test
    fun `increasing size preserves existing samples`() {
        val sut = RollingSampleBuffer(4)
        sut.append(floatArrayOf(1f, 2f, 3f, 4f))

        sut.size = 6

        assertArrayEquals(floatArrayOf(0f, 0f, 1f, 2f, 3f, 4f), sut.current)
    }

    @Test
    fun `decreasing size keeps the most recent samples`() {
        val sut = RollingSampleBuffer(4)
        sut.append(floatArrayOf(1f, 2f, 3f, 4f))

        sut.size = 2

        assertArrayEquals(floatArrayOf(3f, 4f), sut.current)
    }

    // Current
    @Test
    fun `current cannot be mutated externally`() {
        val sut = RollingSampleBuffer(4)
        sut.append(floatArrayOf(1f, 2f, 3f, 4f))

        val snapshot = sut.current
        snapshot[0] = 999f

        assertArrayEquals(floatArrayOf(1f, 2f, 3f, 4f), sut.current)
    }

}