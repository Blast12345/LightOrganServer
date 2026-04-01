package audio.samples

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import toolkit.monkeyTest.nextAudioFormat
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextFloatArray
import toolkit.monkeyTest.nextPositiveInt

class RollingAudioBufferTests {

    private val rollingSampleBuffer: RollingSampleBuffer = mockk()

    private val format1 = nextAudioFormat()
    private val format2 = nextAudioFormat()
    private val format1Frame1 = nextAudioFrame(format = format1)
    private val format1Frame2 = nextAudioFrame(format = format1)
    private val format2Frame = nextAudioFrame(format = format2)

    private val bufferSize = nextPositiveInt()
    private val bufferedSamples = nextFloatArray()

    @BeforeEach
    fun setupHappyPath() {
        every { rollingSampleBuffer.size = any() } just runs
        every { rollingSampleBuffer.size } returns bufferSize
        every { rollingSampleBuffer.current } returns bufferedSamples
        every { rollingSampleBuffer.append(any()) } returns bufferedSamples
        every { rollingSampleBuffer.reset() } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): RollingAudioBuffer {
        return RollingAudioBuffer(rollingSampleBuffer)
    }

    // Current
    @Test
    fun `given no audio has been appended, then current is null`() {
        val sut = createSUT()

        assertNull(sut.current)
    }

    @Test
    fun `given audio has been appended, then get the buffered audio frame`() {
        val sut = createSUT()
        sut.append(format1Frame1)

        val result = sut.current

        assertArrayEquals(bufferedSamples, result?.samples)
        assertEquals(format1, result?.format)
    }

    // Appending
    @Test
    fun `append samples from an audio frame`() {
        val sut = createSUT()

        sut.append(format1Frame1)

        verify { rollingSampleBuffer.append(format1Frame1.samples) }
    }


    @Test
    fun `when appending, return the buffered audio frame`() {
        val sut = createSUT()

        val result = sut.append(format1Frame1)

        assertArrayEquals(bufferedSamples, result.samples)
        assertEquals(format1, result.format)
    }

    // Resetting
    @Test
    fun `reset buffer when the audio format changes`() {
        val sut = createSUT()
        sut.append(format1Frame1)
        clearMocks(rollingSampleBuffer, answers = false) // As written, the initial append() also triggers it

        sut.append(format2Frame)

        verify { rollingSampleBuffer.reset() }
    }

    @Test
    fun `preserves buffer when audio format is unchanged`() {
        val sut = createSUT()
        sut.append(format1Frame1)
        clearMocks(rollingSampleBuffer, answers = false)

        sut.append(format1Frame2)

        verify(exactly = 0) { rollingSampleBuffer.reset() }
    }

    // Size
    @Test
    fun `get the buffer size`() {
        val sut = createSUT()

        val result = sut.size

        assertEquals(bufferSize, result)
    }

    @Test
    fun `set the buffer size`() {
        val sut = createSUT()
        val randomSize = nextPositiveInt()

        sut.size = randomSize

        verify { rollingSampleBuffer.size = randomSize }
    }

}