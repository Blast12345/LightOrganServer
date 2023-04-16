package input

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class AudioBufferTests {

    private val bufferSize = 4
    private val newSamples1 = byteArrayOf(1)
    private val newSamples2 = byteArrayOf(2, 3)

    private fun createSUT(): AudioBuffer {
        return AudioBuffer(bufferSize)
    }

    @Test
    fun `the updated buffer is the same length as the initial size`() {
        val initialSize = Random.nextInt(1024)
        val sut = AudioBuffer(initialSize)
        val updatedSamples = sut.updatedWith(newSamples1)
        assertEquals(initialSize, updatedSamples.size)
    }

    @Test
    fun `the updated buffer follows the first-in-first-out rule`() {
        val sut = createSUT()

        val updatesSamples1 = sut.updatedWith(newSamples1)
        assertArrayEquals(byteArrayOf(0, 0, 0, 1), updatesSamples1)

        val updatesSamples2 = sut.updatedWith(newSamples2)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3), updatesSamples2)
    }

}