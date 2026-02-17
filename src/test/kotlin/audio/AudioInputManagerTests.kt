package audio

import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFinder
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.TestAudioInput
import toolkit.monkeyTest.nextAudioInput

@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputManagerTests {

    private lateinit var audioInput1: TestAudioInput
    private lateinit var audioInput2: TestAudioInput
    private lateinit var allAudioInputs: List<AudioInput>

    private val currentAudioInputFlow = MutableStateFlow<AudioInput?>(null)
    private val audioInputFinder: AudioInputFinder = mockk(relaxed = true)
    private val scope = TestScope()

    @BeforeEach
    fun setupHappyPath() {
        audioInput1 = nextAudioInput("1")
        audioInput2 = nextAudioInput("2")
        allAudioInputs = listOf(audioInput1.mock, audioInput2.mock)

        currentAudioInputFlow.value = null
        every { audioInputFinder.findAll() } returns allAudioInputs
        every { audioInputFinder.findDefaultInput() } returns audioInput1.mock
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): AudioInputManager {
        return AudioInputManager(
            currentAudioInput = currentAudioInputFlow,
            audioInputFinder = audioInputFinder,
            scope = scope,
            sharingPolicy = SharingStarted.Eagerly
        )
    }

    // Input details
    @Test
    fun `given there is no input, then there are no input details`() = runTest {
        val sut = createSUT()

        scope.advanceUntilIdle()

        assertEquals(null, sut.inputDetails.value)
    }

    @Test
    fun `given there is an input, then map the input to details`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = audioInput1.mock
        scope.advanceUntilIdle()

        val expected = AudioInputDetails(audioInput1.mock.name, audioInput1.mock.sampleRate, audioInput1.mock.bitDepth)
        assertEquals(expected, sut.inputDetails.value)
    }

    // Listening state
    @Test
    fun `given there is no input, then listening is false`() = runTest {
        val sut = createSUT()

        scope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `given the input is not listening, then listening is false`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = audioInput1.mock
        scope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `given the input is listening, then listening is true`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = audioInput1.mock
        audioInput1.isListeningFlow.value = true
        scope.advanceUntilIdle()

        assertEquals(true, sut.isListening.value)
    }

    @Test
    fun `when the input changes, then track the new input's listening state`() = runTest {
        val sut = createSUT()

        currentAudioInputFlow.value = audioInput1.mock
        audioInput1.isListeningFlow.value = true
        scope.advanceUntilIdle()

        currentAudioInputFlow.value = audioInput2.mock
        scope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    // Default input
    @Test
    fun `when selecting default input, then find and set the default`() {
        val sut = createSUT()

        sut.selectDefaultInput()

        assertEquals(audioInput1.mock, currentAudioInputFlow.value)
    }

    @Test
    fun `when selecting default input, then stop the current input`() {
        currentAudioInputFlow.value = audioInput2.mock
        val sut = createSUT()

        sut.selectDefaultInput()

        verify { audioInput2.mock.stop() }
    }

    // Start listening
    @Test
    fun `given there is a current input, when start listening is called, then start listening to the current input`() =
        runTest {
            currentAudioInputFlow.value = audioInput1.mock
            val sut = createSUT()

            sut.startListening()

            coVerify { audioInput1.mock.start() }
        }

    @Test
    fun `given there is no current input, when start listening is called, then start listening to the default input`() =
        runTest {
            val sut = createSUT()

            sut.startListening()

            verify { audioInputFinder.findDefaultInput() }
            assertEquals(audioInput1.mock, currentAudioInputFlow.value)
            coVerify { audioInput1.mock.start() }
        }

    // Stop listening
    @Test
    fun `when stop listening is called, then stop listening to the current input`() {
        currentAudioInputFlow.value = audioInput1.mock
        val sut = createSUT()

        sut.stopListening()

        verify { audioInput1.mock.stop() }
    }

}