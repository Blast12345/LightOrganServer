package lightOrgan.input

import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFinder
import audio.audioInput.MockAudioInput
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.extensions.collectInto
import toolkit.monkeyTest.nextAudioFrame


@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputManagerTests {

    private val currentAudioInputFlow = MutableStateFlow<AudioInput?>(null)
    private val audioInputFinder: AudioInputFinder = mockk()
    private val sutScope = TestScope()

    private lateinit var audioInput1: MockAudioInput
    private lateinit var audioInput2: MockAudioInput
    private val allAudioInputs: List<AudioInput> get() = listOf(audioInput1.mock, audioInput2.mock)

    @BeforeEach
    fun setupHappyPath() {
        audioInput1 = MockAudioInput.create("1")
        audioInput2 = MockAudioInput.create("2")

        currentAudioInputFlow.value = null
        every { audioInputFinder.findAll() } returns allAudioInputs
        every { audioInputFinder.findDefaultInput() } returns audioInput1.mock
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
            scope = sutScope,
            sharingPolicy = SharingStarted.Eagerly
        )
    }

    // Input selection
    @Test
    fun `when selecting default input, then find and set the default`() {
        val sut = createSUT()

        sut.selectDefaultInput()

        assertEquals(audioInput1.mock, currentAudioInputFlow.value)
    }

    @Test
    fun `when selecting default input, then stop the current input`() {
        val sut = createSUT()
        currentAudioInputFlow.value = audioInput2.mock

        sut.selectDefaultInput()

        verify { audioInput2.mock.stop() }
    }

    // Input details
    @Test
    fun `given there is no input, then there are no input details`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = null
        sutScope.advanceUntilIdle()

        assertEquals(null, sut.inputDetails.value)
    }

    @Test
    fun `given there is an input, then map the input to details`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = audioInput1.mock
        sutScope.advanceUntilIdle()

        val expected = AudioInputDetails(audioInput1.mock.name, audioInput1.mock.format)
        assertEquals(expected, sut.inputDetails.value)
    }

    // Start listening
    @Test
    fun `given there is a current input, when start listening is called, then start listening to the current input`() {
        val sut = createSUT()
        currentAudioInputFlow.value = audioInput1.mock

        sut.startListening()

        verify { audioInput1.mock.start() }
    }

    @Test
    fun `given there is no current input, when start listening is called, then start listening to the default input`() {
        val sut = createSUT()

        sut.startListening()

        verify { audioInputFinder.findDefaultInput() }
        assertEquals(audioInput1.mock, currentAudioInputFlow.value)
        verify { audioInput1.mock.start() }
    }

    // Stop listening
    @Test
    fun `when stop listening is called, then stop listening to the current input`() {
        val sut = createSUT()
        currentAudioInputFlow.value = audioInput1.mock

        sut.stopListening()

        verify { audioInput1.mock.stop() }
    }

    // Listening state
    @Test
    fun `given there is no input, then listening is false`() = runTest {
        val sut = createSUT()

        sutScope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `given the input is not listening, then listening is false`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = audioInput1.mock
        sutScope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `given the input is listening, then listening is true`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = audioInput1.mock
        audioInput1.isListeningFlow.value = true
        sutScope.advanceUntilIdle()

        assertEquals(true, sut.isListening.value)
    }

    @Test
    fun `when the input changes, then track the new input's listening state`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = audioInput1.mock
        audioInput1.isListeningFlow.value = true
        sutScope.advanceUntilIdle()

        currentAudioInputFlow.value = audioInput2.mock
        sutScope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    // Capture
    @Test
    fun `when an input updates its audio buffer, then the buffered audio is passed on`() = runTest {
        val sut = createSUT()
        val received = sut.bufferedAudio.collectInto(sutScope)

        currentAudioInputFlow.value = audioInput1.mock
        sutScope.advanceUntilIdle()

        val frame = nextAudioFrame()
        audioInput1.bufferedAudioFlow.emit(frame)
        sutScope.advanceUntilIdle()

        assertEquals(listOf(frame), received)
    }

    @Test
    fun `when the input changes, then the new inputs buffered audio is passed on`() = runTest {
        val sut = createSUT()
        val input1Frame = nextAudioFrame()
        val input2Frame = nextAudioFrame()
        val received = sut.bufferedAudio.collectInto(sutScope)

        currentAudioInputFlow.value = audioInput1.mock
        sutScope.advanceUntilIdle()
        audioInput1.bufferedAudioFlow.emit(input1Frame)
        sutScope.advanceUntilIdle()
        currentAudioInputFlow.value = audioInput2.mock
        sutScope.advanceUntilIdle()
        audioInput2.bufferedAudioFlow.emit(input2Frame)
        sutScope.advanceUntilIdle()

        assertEquals(listOf(input1Frame, input2Frame), received)
    }

}