package audio.audioInput

import audio.samples.SampleNormalizer
import io.mockk.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.extensions.collectInto
import toolkit.monkeyTest.*
import wrappers.sound.InputLine

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class AudioInputTests {

    private val inputLine: InputLine = mockk()
    private val sampleNormalizer: SampleNormalizer = mockk()
    private val sutScope = TestScope()

    private val readResults = List(3) { nextInputLineReadResult() }
    private val resultChannel = Channel<InputLine.ReadResult>(Channel.UNLIMITED)
    private val normalizedSamples = nextFloatArray()

    @BeforeEach
    fun setupHappyPath() {
        every { inputLine.name } returns nextString("name")
        every { inputLine.sampleRate } returns nextPositiveFloat()
        every { inputLine.bitDepth } returns nextInt()
        every { inputLine.channels } returns nextInt()

        every { inputLine.start() } answers {
            coEvery { inputLine.read() } coAnswers { resultChannel.receive() }
        }
        every { inputLine.stop() } returns Unit

        every { sampleNormalizer.normalize(any()) } returns normalizedSamples
    }

    @AfterEach
    fun tearDown() {
        sutScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): AudioInput {
        return AudioInput(
            inputLine = inputLine,
            sampleNormalizer = sampleNormalizer,
            scope = sutScope,
        )
    }

    // Details
    @Test
    fun `get the name`() {
        val sut = createSUT()

        val actual = sut.name

        assertEquals(inputLine.name, actual)
    }

    @Test
    fun `get the sample rate`() {
        val sut = createSUT()

        val actual = sut.format.sampleRate

        assertEquals(inputLine.sampleRate, actual)
    }

    @Test
    fun `get the bit depth`() {
        val sut = createSUT()

        val actual = sut.format.bitDepth

        assertEquals(inputLine.bitDepth, actual)
    }

    @Test
    fun `get the number of channels`() {
        val sut = createSUT()

        val actual = sut.format.channels

        assertEquals(inputLine.channels, actual)
    }

    // Start listening
    @Test
    fun `start continuously capturing audio`() {
        val sut = createSUT()
        val received = sut.audioStream.collectInto(sutScope)
        sut.start()
        sutScope.advanceUntilIdle()

        readResults.forEach { resultChannel.trySend(it) }
        sutScope.advanceUntilIdle()

        assertEquals(readResults.size, received.size)
        for (result in received) {
            assertEquals(normalizedSamples, result.audio.samples)
            assertEquals(sut.format, result.audio.format)
        }
    }

    @Test
    fun `when start is called multiple times, the audio stream is not duplicated`() {
        val sut = createSUT()

        sut.start()
        sutScope.advanceUntilIdle()
        sut.start()
        sutScope.advanceUntilIdle()

        // Weak assertion, but I'm not aware of better option
        verify(exactly = 1) { inputLine.start() }
    }

    @Test
    fun `when the input fails to start, then the error is thrown`() {
        val sut = createSUT()
        every { inputLine.start() } throws nextException()

        assertThrows<Exception> { sut.start() }
    }

    // Stop
    @Test
    fun `stop capturing audio`() {
        val sut = createSUT()
        sut.start()
        sutScope.advanceUntilIdle()

        sut.stop()
        sutScope.advanceUntilIdle()

        verify { inputLine.stop() }
    }

    // Listening state
    @Test
    fun `get the listening state`() {
        val sut = createSUT()

        sut.start()
        sutScope.advanceUntilIdle()
        assertEquals(true, sut.isListening.value)

        sut.stop()
        sutScope.advanceUntilIdle()
        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `when reading the input fails, then listening is false`() {
        val sut = createSUT()
        every { inputLine.start() } answers {
            coEvery { inputLine.read() } throws nextException()
        }

        sut.start()
        sutScope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

}
