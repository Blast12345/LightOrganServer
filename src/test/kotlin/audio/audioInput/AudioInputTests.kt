package audio.audioInput

import audio.samples.SampleBuffer
import audio.samples.SampleNormalizer
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.extensions.collectInto
import toolkit.monkeyTest.*
import wrappers.sound.InputLine

@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputTests {

    private val inputLine: InputLine = mockk()
    private val sampleNormalizer: SampleNormalizer = mockk()
    private val sampleBuffer: SampleBuffer = mockk()
    private val scope = TestScope()

    private val readTime = nextDuration()
    private val readData = nextByteArray()
    private val normalizedSamples = nextFloatArray()
    private val bufferedSamples = nextFloatArray()

    @BeforeEach
    fun setupHappyPath() {
        every { inputLine.name } returns nextString("name")
        every { inputLine.sampleRate } returns nextPositiveInt()
        every { inputLine.bitDepth } returns nextPositiveInt()
        every { inputLine.channels } returns nextPositiveInt()

        every { inputLine.start() } coAnswers {
            coEvery { inputLine.read() } coAnswers { delay(readTime); readData }
        }
        every { inputLine.stop() } returns Unit

        every { sampleNormalizer.normalize(readData) } returns normalizedSamples
        every { sampleBuffer.append(normalizedSamples) } returns Unit
        every { sampleBuffer.current } returns bufferedSamples
    }

    @AfterEach
    fun tearDown() {
        scope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): AudioInput {
        return AudioInput(
            inputLine = inputLine,
            sampleNormalizer = sampleNormalizer,
            sampleBuffer = sampleBuffer,
            scope = scope
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
    fun `when started, then continuously capture audio`() = runTest {
        val sut = createSUT()
        val iterations = nextPositiveInt(max = 5)
        val received = sut.bufferedAudio.collectInto(scope)

        sut.start()

        repeat(iterations) {
            scope.advanceTimeBy(readTime)
            scope.runCurrent()
        }

        // Ideally, I'd verify all received values, but the test complexity didn't seem worth it.
        val firstAudio = received.first()
        assertEquals(bufferedSamples, firstAudio.samples)
        assertEquals(sut.format, firstAudio.format)
        assertEquals(iterations, received.size)
    }

    @Test
    fun `given already listening, when started again, then audio output is not duplicated`() =
        runTest {
            val sut = createSUT()
            val iterations = nextPositiveInt(max = 5)
            val received = sut.bufferedAudio.collectInto(scope)

            sut.start()
            sut.start()

            repeat(iterations) {
                scope.advanceTimeBy(readTime)
                scope.runCurrent()
            }

            assertEquals(iterations, received.size)
        }

    // Stop
    @Test
    fun `stop capturing audio`() = runTest {
        val sut = createSUT()
        val iterations = 3
        val received = sut.bufferedAudio.collectInto(scope)

        sut.start()
        repeat(iterations) {
            scope.advanceTimeBy(readTime)
            scope.runCurrent()
        }

        sut.stop()
        repeat(iterations) {
            scope.advanceTimeBy(readTime)
            scope.runCurrent()
        }


        assertEquals(iterations, received.size)
        verify { inputLine.stop() }
    }

    // Listening state
    @Test
    fun `when started, then listening is true`() = runTest {
        val sut = createSUT()

        sut.start()
        scope.runCurrent()

        assertEquals(true, sut.isListening.value)
    }

    @Test
    fun `when the input fails to activate, then listening is false`() = runTest {
        val sut = createSUT()
        every { inputLine.start() } throws nextException()

        runCatching { sut.start() }

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `when reading the input fails, then listening is false`() = runTest {
        val sut = createSUT()
        every { inputLine.start() } coAnswers {
            coEvery { inputLine.read() } throws nextException()
        }

        sut.start()
        scope.advanceTimeBy(readTime)
        scope.runCurrent()

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `when stopped, then listening is false`() = runTest {
        val sut = createSUT()
        sut.start()

        sut.stop()

        assertEquals(false, sut.isListening.value)
    }

}