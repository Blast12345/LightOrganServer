package audio.samples

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFormat

@OptIn(ExperimentalCoroutinesApi::class)
class AccumulatingAudioBufferTests {

    val format1 = nextAudioFormat()
    val format2 = nextAudioFormat()
    val format1Frame1 = AudioFrame(samples = floatArrayOf(1F, 2F, 3F), format1)
    val format1Frame2 = AudioFrame(samples = floatArrayOf(4F, 5F, 6F), format1)
    val format2Frame1 = AudioFrame(samples = floatArrayOf(7F, 8F, 9F), format2)

    @Test
    fun `drain returns the appended frame`() = runTest {
        val sut = AccumulatingAudioBuffer()

        sut.append(format1Frame1)
        val actual = sut.drain()

        assertArrayEquals(format1Frame1.samples, actual.samples)
        assertEquals(format1, actual.format)
    }

    @Test
    fun `drain concatenates multiple appended frames`() = runTest {
        val sut = AccumulatingAudioBuffer()

        sut.append(format1Frame1)
        sut.append(format1Frame2)
        val actual = sut.drain()

        assertArrayEquals(format1Frame1.samples + format1Frame2.samples, actual.samples)
        assertEquals(format1, actual.format)
    }

    @Test
    fun `drain suspends until a frame is appended`() = runTest {
        val sut = AccumulatingAudioBuffer()

        val result = async { sut.drain() }
        advanceUntilIdle()
        assertFalse(result.isCompleted)

        sut.append(format1Frame1)
        advanceUntilIdle()
        assertTrue(result.isCompleted)

        assertArrayEquals(format1Frame1.samples, result.await().samples)
    }

    @Test
    fun `appending a different format clears previous frames`() = runTest {
        val sut = AccumulatingAudioBuffer()

        sut.append(format1Frame1)
        sut.append(format2Frame1)
        val actual = sut.drain()

        assertArrayEquals(format2Frame1.samples, actual.samples)
        assertEquals(format2, actual.format)
    }

}