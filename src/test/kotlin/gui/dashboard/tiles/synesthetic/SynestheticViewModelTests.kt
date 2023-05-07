package gui.dashboard.tiles.synesthetic

import LightOrganStateMachine
import androidx.compose.runtime.MutableState
import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class SynestheticViewModelTests {

    private val startAutomaticallyState: MutableState<Boolean> = mockk(relaxed = true)
    private val isRunningState: MutableState<Boolean> = mockk(relaxed = true)
    private val lightOrganStateMachine: LightOrganStateMachine = mockk(relaxed = true)
    private val config: Config = mockk(relaxed = true)

    private val startAutomatically = Random.nextBoolean()
    private val isRunning = Random.nextBoolean()

    @BeforeEach
    fun setup() {
        every { startAutomaticallyState.value } returns startAutomatically
        every { isRunningState.value } returns isRunning
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): SynestheticViewModel {
        return SynestheticViewModel(
            startAutomatically = startAutomaticallyState,
            isRunning = isRunningState,
            lightOrganStateMachine = lightOrganStateMachine,
            config = config
        )
    }

    @Test
    fun `toggling start automatically flips the value of the mutable state`() {
        val sut = createSUT()
        sut.toggleStartAutomatically()
        verify { startAutomaticallyState.value = !startAutomatically }
    }

    @Test
    fun `toggling start automatically flips the value of the config`() {
        val sut = createSUT()
        sut.toggleStartAutomatically()
        verify { config.startAutomatically = !startAutomatically }
    }

    @Test
    fun `start the light organ`() {
        val sut = createSUT()
        sut.start()
        verify { lightOrganStateMachine.start() }
    }

    @Test
    fun `start sets the running state to true`() {
        val sut = createSUT()
        sut.start()
        verify { isRunningState.value = true }
    }

    @Test
    fun `stop the light organ`() {
        val sut = createSUT()
        sut.stop()
        verify { lightOrganStateMachine.stop() }
    }

    @Test
    fun `stop sets the running state to false`() {
        val sut = createSUT()
        sut.stop()
        verify { isRunningState.value = false }
    }

}