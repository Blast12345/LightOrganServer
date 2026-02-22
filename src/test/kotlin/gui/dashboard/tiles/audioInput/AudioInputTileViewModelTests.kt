package gui.dashboard.tiles.audioInput

import gui.dashboard.SnackbarController
import gui.tiles.audioInput.AudioInputTileViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lightOrgan.input.AudioInputDetails
import lightOrgan.input.AudioInputManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioInputDetails
import toolkit.monkeyTest.nextException

@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputTileViewModelTests {

    private val audioInputManager: AudioInputManager = mockk()
    private val snackbarController: SnackbarController = mockk()
    private val sutScope = TestScope()

    private val audioInputDetailsFlow = MutableStateFlow<AudioInputDetails?>(null)
    private val isListeningFlow = MutableStateFlow(false)

    private val inputDetails = nextAudioInputDetails()
    private val exceptionWithMessage = nextException()
    private val exceptionWithoutMessage = Exception()

    @BeforeEach
    fun setup() {
        every { audioInputManager.inputDetails } returns audioInputDetailsFlow
        every { audioInputManager.isListening } returns isListeningFlow

        audioInputDetailsFlow.value = null
        isListeningFlow.value = false
    }

    @AfterEach
    fun tearDown() {
        sutScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): AudioInputTileViewModel {
        return AudioInputTileViewModel(
            audioInputManager = audioInputManager,
            snackbarController = snackbarController,
            scope = sutScope
        )
    }

    // Input details
    @Test
    fun `given there is no input, then there are no input details`() = runTest {
        val sut = createSUT()

        sutScope.advanceUntilIdle()

        assertEquals(null, sut.inputDetails.value)
    }

    @Test
    fun `given there is an input, then there are input details`() = runTest {
        val sut = createSUT()

        audioInputDetailsFlow.value = inputDetails
        sutScope.advanceUntilIdle()

        assertEquals(inputDetails, sut.inputDetails.value)
    }

    // Listening state
    @Test
    fun `given the input manager is not listening, then listening is false`() = runTest {
        val sut = createSUT()

        sutScope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `given the input manager is listening, then listening is true`() = runTest {
        val sut = createSUT()
        isListeningFlow.value = true

        sutScope.advanceUntilIdle()

        assertEquals(true, sut.isListening.value)
    }

    // Connect
    @Test
    fun `when connect is called, then start listening`() {
        val sut = createSUT()

        sut.connect()
        sutScope.advanceUntilIdle()

        coVerify { audioInputManager.startListening() }
    }

    @Test
    fun `when connect fails with an error, then show the error`() {
        val sut = createSUT()
        coEvery { audioInputManager.startListening() } throws exceptionWithMessage

        sut.connect()
        sutScope.advanceUntilIdle()

        coVerify { snackbarController.show(exceptionWithMessage.message!!) }
    }

    @Test
    fun `when connect fails without an error, then show a generic error`() {
        val sut = createSUT()
        coEvery { audioInputManager.startListening() } throws exceptionWithoutMessage

        sut.connect()
        sutScope.advanceUntilIdle()

        coVerify { snackbarController.show("Failed to connect to input.") }
    }

    // Disconnect
    @Test
    fun `when disconnect is called, then stop listening`() {
        val sut = createSUT()

        sut.disconnect()

        verify { audioInputManager.stopListening() }
    }

    @Test
    fun `when disconnect fails with an error, then show the error`() = runTest {
        val sut = createSUT()
        every { audioInputManager.stopListening() } throws exceptionWithMessage

        sut.disconnect()
        sutScope.advanceUntilIdle()

        coVerify { snackbarController.show(exceptionWithMessage.message!!) }
    }

    @Test
    fun `when disconnect fails without an error, then show a generic error`() = runTest {
        val sut = createSUT()
        every { audioInputManager.stopListening() } throws exceptionWithoutMessage

        sut.disconnect()
        sutScope.advanceUntilIdle()

        coVerify { snackbarController.show("Failed to disconnect from input.") }
    }

}