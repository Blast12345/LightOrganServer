package input.audioInput

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextByteArray
import toolkit.monkeyTest.nextFloatArray
import toolkit.monkeyTest.nextPositiveInt
import toolkit.monkeyTest.nextString
import wrappers.sound.InputLine

@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputTests {

    private val inputLine: InputLine = mockk()
    private val sampleNormalizer: SampleNormalizer = mockk()
    private val sampleBuffer: SampleBuffer = mockk()
    private val scope = TestScope()

    private val inputData = nextByteArray()
    private val normalizedSamples = nextFloatArray()
    private val bufferedSamples = nextFloatArray()

    @BeforeEach
    fun setupHappyPath() {
        every { inputLine.name } returns nextString("name")
        every { inputLine.sampleRate } returns nextPositiveInt()
        every { inputLine.bitDepth } returns nextPositiveInt()
        every { inputLine.channels } returns nextPositiveInt()

        coEvery { inputLine.start() } coAnswers {
            coEvery { inputLine.read() } coAnswers { inputData } coAndThen { suspendCancellableCoroutine { } }
        }
        every { inputLine.stop() } returns Unit

        every { sampleNormalizer.normalize(inputData) } returns normalizedSamples
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

        val actual = sut.sampleRate

        assertEquals(inputLine.sampleRate, actual)
    }

    @Test
    fun `get the bit depth`() {
        val sut = createSUT()

        val actual = sut.bitDepth

        assertEquals(inputLine.bitDepth, actual)
    }

    // Start listening
    @Test
    fun `start capturing samples`() = runTest {
        val sut = createSUT()
        val received = mutableListOf<FloatArray>()
        sut.sampleUpdates.onEach { received.add(it) }.launchIn(scope)

        sut.start()
        scope.runCurrent()

        assertEquals(listOf(bufferedSamples), received)
    }

    @Test
    fun `when started, then listening is true`() = runTest {
        val sut = createSUT()

        sut.start()
        // don't runCurrent or the read loop will start

        assertEquals(true, sut.isListening.value)
    }

    @Test
    fun `when failed to start, then listening is false`() = runTest {
        coEvery { inputLine.start() } throws RuntimeException("failed to open")
        val sut = createSUT()

        runCatching { sut.start() }

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `when sample capturing fails, then listening is false`() = runTest {
        coEvery { inputLine.start() } coAnswers {
            coEvery { inputLine.read() } throws RuntimeException("read failed")
        }
        val sut = createSUT()

        sut.start()
        scope.runCurrent()

        assertEquals(false, sut.isListening.value)
    }

    // Stop
    @Test
    fun `stop capturing samples`() = runTest {
        val sut = createSUT()
        sut.start()

        sut.stop()
        scope.runCurrent()

        // NOTE: Because runCurrent happened after stop - the loop is canceled before it has a chance to iterate.
        coVerify(exactly = 0) { inputLine.read() }
        verify { inputLine.stop() }
    }

    @Test
    fun `when stopped, then listening is false`() = runTest {
        val sut = createSUT()
        sut.start()

        sut.stop()

        assertEquals(false, sut.isListening.value)
    }

}