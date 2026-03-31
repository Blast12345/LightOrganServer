package audio.audioInput

import audio.samples.SampleNormalizer
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.assertions.eventually
import toolkit.extensions.collectInto
import toolkit.monkeyTest.*
import wrappers.sound.InputLine
import java.util.concurrent.LinkedBlockingQueue

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class AudioInputTests {

    private val inputLine: InputLine = mockk()
    private val sampleNormalizer: SampleNormalizer = mockk()
    private val sutScope = CoroutineScope(SupervisorJob() + newSingleThreadContext("TestAudioCapture"))

    private val readResults = List(3) { nextInputLineReadResult() }
    private val resultQueue = LinkedBlockingQueue<InputLine.ReadResult>()
    private val normalizedSamples = nextFloatArray()

    private val collectionScope = CoroutineScope(SupervisorJob())

    @BeforeEach
    fun setupHappyPath() {
        every { inputLine.name } returns nextString("name")
        every { inputLine.sampleRate } returns nextPositiveFloat()
        every { inputLine.bitDepth } returns nextPositiveInt()
        every { inputLine.channels } returns nextPositiveInt()

        every { inputLine.start() } answers {
            every { inputLine.read() } answers { resultQueue.take() }
        }
        every { inputLine.stop() } returns Unit

        every { sampleNormalizer.normalize(any()) } returns normalizedSamples
    }

    @AfterEach
    fun tearDown() {
        sutScope.cancel()
        collectionScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): AudioInput {
        return AudioInput(
            inputLine = inputLine,
            sampleNormalizer = sampleNormalizer,
            scope = sutScope
        )
    }

    private fun populateReadResultsQueue() {
        for (result in readResults) {
            resultQueue.put(result)
        }
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
    fun `when started, then continuously capture audio`() {
        val sut = createSUT()
        val received = sut.audioStream.collectInto(collectionScope)

        sut.start()
        populateReadResultsQueue()

        eventually {
            assertEquals(readResults.size, received.size)

            for (result in received) {
                assertEquals(normalizedSamples, result.audio.samples)
                assertEquals(sut.format, result.audio.format)
            }
        }
    }

    @Test
    fun `given already listening, when started again, then audio stream is not duplicated`() {
        val sut = createSUT()
        val received = sut.audioStream.collectInto(collectionScope)

        sut.start()
        sut.start()
        populateReadResultsQueue()

        eventually { assertEquals(readResults.size, received.size) }
    }

    // Stop
    @Test
    fun `stop capturing audio`() {
        val sut = createSUT()

        sut.start()
        sut.stop()

        eventually { verify { inputLine.stop() } }
    }

    // Listening state
    @Test
    fun `the listening state reflects start and stop`() {
        val sut = createSUT()

        sut.start()
        assertEquals(true, sut.isListening.value)

        sut.stop()
        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `when the input fails to activate, then listening is false`() {
        val sut = createSUT()
        every { inputLine.start() } throws nextException()

        runCatching { sut.start() }

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `when reading the input fails, then listening is false`() {
        val sut = createSUT()
        every { inputLine.start() } answers {
            every { inputLine.read() } throws nextException()
        }

        sut.start()

        eventually { assertEquals(false, sut.isListening.value) }
    }

}
