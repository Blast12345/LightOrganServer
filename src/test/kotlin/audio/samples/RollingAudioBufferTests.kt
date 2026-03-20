package audio.samples

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFormat
import toolkit.monkeyTest.nextPositiveInt

class RollingAudioBufferTests {

    private val randomSize = nextPositiveInt()
    private val format1 = nextAudioFormat()
    private val format2 = nextAudioFormat()

    private val format1Frame1 = AudioFrame(samples = floatArrayOf(1f, 2f), format1)
    private val format1Frame2 = AudioFrame(samples = floatArrayOf(3f, 4f), format1)
    private val format2Frame1 = AudioFrame(samples = floatArrayOf(5f, 6f), format2)

    @Test
    fun `appending frame smaller than capacity is padded with leading zeros`() {
        val sut = RollingAudioBuffer(capacity = 4)

        val frame = AudioFrame(floatArrayOf(1f, 2f), format1)
        val actual = sut.append(frame)

        assertArrayEquals(floatArrayOf(0f, 0f, 1f, 2f), actual.samples)
    }

    @Test
    fun `appending frame equal to capacity fills the buffer exactly`() {
        val sut = RollingAudioBuffer(capacity = 3)

        val frame = AudioFrame(floatArrayOf(1f, 2f, 3f), format1)
        val actual = sut.append(frame)

        assertArrayEquals(floatArrayOf(1f, 2f, 3f), actual.samples)
    }

    @Test
    fun `appending frame larger than capacity keeps only the final samples`() {
        val sut = RollingAudioBuffer(capacity = 2)

        val frame = AudioFrame(floatArrayOf(1f, 2f, 3f, 4f, 5f), format1)
        val actual = sut.append(frame)

        assertArrayEquals(floatArrayOf(4f, 5f), actual.samples)
    }

    @Test
    fun `appending multiple frames of the same format`() {
        val sut = RollingAudioBuffer(capacity = 4)

        val actual1 = sut.append(format1Frame1)
        assertArrayEquals(floatArrayOf(0f, 0f, 1f, 2f), actual1.samples)
        assertEquals(format1, actual1.format)


        val actual2 = sut.append(format1Frame2)
        assertArrayEquals(floatArrayOf(1f, 2f, 3f, 4f), actual2.samples)
        assertEquals(format1, actual2.format)
    }

    @Test
    fun `appending multiple frames of different forms clears previous samples`() {
        val sut = RollingAudioBuffer(capacity = 5)

        val actual1 = sut.append(format1Frame1)
        assertArrayEquals(floatArrayOf(0f, 0f, 0f, 1f, 2f), actual1.samples)
        assertEquals(format1, actual1.format)

        val actual2 = sut.append(AudioFrame(floatArrayOf(3f, 4f), format2))
        assertArrayEquals(floatArrayOf(0f, 0f, 0f, 3f, 4f), actual2.samples)
        assertEquals(format2, actual2.format)
    }

}