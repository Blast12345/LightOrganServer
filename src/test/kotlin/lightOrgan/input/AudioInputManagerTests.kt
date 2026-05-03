package lightOrgan.input

import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFinder
import audio.audioInput.AudioInputFixture
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import toolkit.extensions.collectInto
import toolkit.monkeyTest.nextAudioStreamFrame


@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputManagerTests {

    private val currentAudioInputFlow = MutableStateFlow<AudioInput?>(null)
    private val audioInputFinder: AudioInputFinder = mockk()
    private val sutScope = TestScope()

    private lateinit var defaultInput: AudioInputFixture
    private lateinit var otherInput: AudioInputFixture
    private val allAudioInputs: List<AudioInput> get() = listOf(defaultInput.mock, otherInput.mock)

    @BeforeEach
    fun setupHappyPath() {
        defaultInput = AudioInputFixture.create("default")
        otherInput = AudioInputFixture.create("other")

        currentAudioInputFlow.value = null
        every { audioInputFinder.findAll() } returns allAudioInputs
        every { audioInputFinder.findDefaultInput() } returns defaultInput.mock
    }

    @AfterEach
    fun tearDown() {
        sutScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): AudioInputManager {
        return AudioInputManager(
            currentAudioInput = currentAudioInputFlow,
            audioInputFinder = audioInputFinder,
            scope = sutScope
        )
    }

    // Input Details
    @Test
    fun `get the input details`() = runTest {
        val sut = createSUT()
        currentAudioInputFlow.value = defaultInput.mock
        sutScope.advanceUntilIdle()

        assertEquals(defaultInput.details, sut.inputDetails.value)
    }

    @Test
    fun `given there is no input, then there are no input details`() = runTest {
        val sut = createSUT()
        currentAudioInputFlow.value = null
        sutScope.advanceUntilIdle()

        assertNull(sut.inputDetails.value)
    }

    @Test
    fun `when the input changes, then the input details reflect the new input`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = defaultInput.mock
        sutScope.advanceUntilIdle()

        currentAudioInputFlow.value = otherInput.mock
        sutScope.advanceUntilIdle()

        assertEquals(otherInput.details, sut.inputDetails.value)
    }

    // Input selection
    @Test
    fun `select the default input`() = runTest {
        val sut = createSUT()

        sut.selectDefaultInput()
        sutScope.advanceUntilIdle()

        assertEquals(defaultInput.details, sut.inputDetails.value)
    }

    @Test
    fun `given an input was already selected, then selecting the default stops the previous input`() {
        val sut = createSUT()
        currentAudioInputFlow.value = otherInput.mock

        sut.selectDefaultInput()

        verify { otherInput.mock.stop() }
    }

    // Start listening
    @Test
    fun `start listening to the current input`() {
        val sut = createSUT()
        currentAudioInputFlow.value = defaultInput.mock

        sut.startListening()

        verify { defaultInput.mock.start() }
    }

    @Test
    fun `given there is no input, then starting throws an error`() {
        val sut = createSUT()

        assertThrows<IllegalStateException> { sut.startListening() }
    }

    // Stop listening
    @Test
    fun `stop listening to the current input`() {
        val sut = createSUT()
        currentAudioInputFlow.value = defaultInput.mock

        sut.stopListening()

        verify { defaultInput.mock.stop() }
    }

    @Test
    fun `given there is no input, then stopping throws an error`() {
        val sut = createSUT()

        assertThrows<IllegalStateException> { sut.stopListening() }
    }

    // Listening state
    @Test
    fun `get the inputs listening state `() = runTest {
        val sut = createSUT()
        currentAudioInputFlow.value = defaultInput.mock
        sutScope.advanceUntilIdle()

        defaultInput.isListeningFlow.value = true
        sutScope.advanceUntilIdle()
        assertEquals(true, sut.isListening.value)

        defaultInput.isListeningFlow.value = false
        sutScope.advanceUntilIdle()
        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `given there is no input, then listening is false`() = runTest {
        val sut = createSUT()
        currentAudioInputFlow.value = null
        sutScope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `when the input changes, then listening reflects the new inputs state`() = runTest {
        val sut = createSUT()
        currentAudioInputFlow.value = defaultInput.mock
        defaultInput.isListeningFlow.value = true
        sutScope.advanceUntilIdle()

        currentAudioInputFlow.value = otherInput.mock
        otherInput.isListeningFlow.value = false
        sutScope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    // Capture
    @Test
    fun `when an input emits new audio, then the audio is passed on`() = runTest {
        val sut = createSUT()
        val received = sut.audioStream.collectInto(sutScope)

        currentAudioInputFlow.value = defaultInput.mock
        sutScope.advanceUntilIdle()

        val frame = nextAudioStreamFrame()
        defaultInput.audioStreamFlow.emit(frame)
        sutScope.advanceUntilIdle()

        assertEquals(listOf(frame), received)
    }

    @Test
    fun `when the input changes, then the new inputs audio is passed on`() = runTest {
        val sut = createSUT()
        val defaultFrame = nextAudioStreamFrame()
        val otherFrame = nextAudioStreamFrame()
        val received = sut.audioStream.collectInto(sutScope)

        currentAudioInputFlow.value = defaultInput.mock
        sutScope.advanceUntilIdle()
        defaultInput.audioStreamFlow.emit(defaultFrame)
        sutScope.advanceUntilIdle()
        currentAudioInputFlow.value = otherInput.mock
        sutScope.advanceUntilIdle()
        otherInput.audioStreamFlow.emit(otherFrame)
        sutScope.advanceUntilIdle()

        assertEquals(listOf(defaultFrame, otherFrame), received)
    }

}