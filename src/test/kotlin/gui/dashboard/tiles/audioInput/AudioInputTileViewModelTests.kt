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
import org.junit.jupiter.api.Assertions.assertNull
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
        every { audioInputManager.selectDefaultInput() } returns Unit
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

    // Current input
    @Test
    fun `find the default input`() = runTest {
        val sut = createSUT()

        sut.findInput()

        verify { audioInputManager.selectDefaultInput() }
    }

    // Input Details
    @Test
    fun `given no input details, then no input details are displayed`() = runTest {
        val sut = createSUT()

        sutScope.advanceUntilIdle()

        assertNull(sut.inputDetails.value)
    }

    @Test
    fun `given input details exist, then input details are displayed`() = runTest {
        audioInputDetailsFlow.value = inputDetails

        val sut = createSUT()
        sutScope.advanceUntilIdle()

        assertEquals(inputDetails, sut.inputDetails.value)
    }

    @Test
    fun `when input details change, then displayed input details reflect the change`() = runTest {
        val sut = createSUT()
        sutScope.advanceUntilIdle()

        audioInputDetailsFlow.value = inputDetails
        sutScope.advanceUntilIdle()

        assertEquals(inputDetails, sut.inputDetails.value)
    }

    // Listening state
    @Test
    fun `given the input is not listening, then not listening is displayed`() = runTest {
        val sut = createSUT()

        sutScope.advanceUntilIdle()

        assertEquals(false, sut.isListening.value)
    }

    @Test
    fun `given the input is listening, then listening is displayed`() = runTest {
        isListeningFlow.value = true

        val sut = createSUT()
        sutScope.advanceUntilIdle()

        assertEquals(true, sut.isListening.value)
    }

    @Test
    fun `when the inputs listening state changes, then the displayed listening state reflects the change`() = runTest {
        val sut = createSUT()
        sutScope.advanceUntilIdle()

        isListeningFlow.value = true
        sutScope.advanceUntilIdle()

        assertEquals(true, sut.isListening.value)
    }

    // Start
    @Test
    fun `start listening to the input`() {
        val sut = createSUT()

        sut.start()
        sutScope.advanceUntilIdle()

        coVerify { audioInputManager.startListening() }
    }

    @Test
    fun `when start fails with an error, then show the error`() {
        val sut = createSUT()
        coEvery { audioInputManager.startListening() } throws exceptionWithMessage

        sut.start()
        sutScope.advanceUntilIdle()

        coVerify { snackbarController.show(exceptionWithMessage.message!!) }
    }

    @Test
    fun `when start fails without an error, then show a generic error`() {
        val sut = createSUT()
        coEvery { audioInputManager.startListening() } throws exceptionWithoutMessage

        sut.start()
        sutScope.advanceUntilIdle()

        coVerify { snackbarController.show("Failed to connect to input.") }
    }

    // Stop
    @Test
    fun `stop listening to the input`() {
        val sut = createSUT()

        sut.stop()

        verify { audioInputManager.stopListening() }
    }

    @Test
    fun `when stop fails with an error, then show the error`() = runTest {
        val sut = createSUT()
        every { audioInputManager.stopListening() } throws exceptionWithMessage

        sut.stop()
        sutScope.advanceUntilIdle()

        coVerify { snackbarController.show(exceptionWithMessage.message!!) }
    }

    @Test
    fun `when stop fails without an error, then show a generic error`() = runTest {
        val sut = createSUT()
        every { audioInputManager.stopListening() } throws exceptionWithoutMessage

        sut.stop()
        sutScope.advanceUntilIdle()

        coVerify { snackbarController.show("Failed to disconnect from input.") }
    }

}