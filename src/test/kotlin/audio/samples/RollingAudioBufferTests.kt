package audio.samples

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import toolkit.monkeyTest.nextAudioFormat
import toolkit.monkeyTest.nextPositiveInt

class RollingAudioBufferTests {

    private val randomSize = nextPositiveInt()
    private val format1 = nextAudioFormat()
    private val format2 = nextAudioFormat()

    @Test
    fun `current is null by default`() {
        val sut = RollingAudioBuffer(randomSize)

        assertNull(sut.current)
    }

    // Samples
    @Test
    fun `appending frame smaller than capacity is padded with leading zeros`() {
        val sut = RollingAudioBuffer(capacity = 4)

        val frame = AudioFrame(floatArrayOf(1f, 2f), format1)
        sut.append(frame)

        assertArrayEquals(floatArrayOf(0f, 0f, 1f, 2f), sut.current?.samples)
    }

    @Test
    fun `appending frame equal to capacity fills the buffer exactly`() {
        val sut = RollingAudioBuffer(capacity = 3)

        val frame = AudioFrame(floatArrayOf(1f, 2f, 3f), format1)
        sut.append(frame)

        assertArrayEquals(floatArrayOf(1f, 2f, 3f), sut.current?.samples)
    }

    @Test
    fun `appending frame larger than capacity keeps only the final samples`() {
        val sut = RollingAudioBuffer(capacity = 2)

        val frame = AudioFrame(floatArrayOf(1f, 2f, 3f, 4f, 5f), format1)
        sut.append(frame)

        assertArrayEquals(floatArrayOf(4f, 5f), sut.current?.samples)
    }

    @Test
    fun `appending a different format clears previous samples`() {
        val sut = RollingAudioBuffer(capacity = 5)

        sut.append(AudioFrame(floatArrayOf(1f, 2f), format1))
        sut.append(AudioFrame(floatArrayOf(3f, 4f), format2))

        assertArrayEquals(floatArrayOf(0f, 0f, 0f, 3f, 4f), sut.current?.samples)
    }

    // Format
    @Test
    fun `current format matches the most recently appended frame`() {
        val sut = RollingAudioBuffer(randomSize)

        sut.append(AudioFrame(floatArrayOf(1f, 2f), format1))
        assertEquals(format1, sut.current?.format)

        sut.append(AudioFrame(floatArrayOf(3f, 4f), format2))
        assertEquals(format2, sut.current?.format)
    }

}