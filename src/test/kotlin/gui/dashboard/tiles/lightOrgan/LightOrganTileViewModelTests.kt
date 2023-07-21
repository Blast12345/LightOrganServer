package gui.dashboard.tiles.lightOrgan

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.LightOrganStateMachine
import lightOrgan.LightOrganSubscriber
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class LightOrganTileViewModelTests {

    private val startAutomatically: MutableStateFlow<Boolean> = mockk()
    private val isRunning: MutableStateFlow<Boolean> = mockk(relaxed = true)
    private val lightOrganStateMachine: LightOrganStateMachine = mockk(relaxed = true)

    private val newSubscriber: LightOrganSubscriber = mockk()

    @BeforeEach
    fun setup() {
        every { startAutomatically.value } returns Random.nextBoolean()
        every { lightOrganStateMachine.start() } returns Unit
        every { lightOrganStateMachine.stop() } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrganTileViewModel {
        return LightOrganTileViewModel(
            startAutomatically = startAutomatically,
            isRunning = isRunning,
            lightOrganStateMachine = lightOrganStateMachine
        )
    }

    @Test
    fun `the light organ starts automatically when the start automatically is true`() {
        every { startAutomatically.value } returns true
        createSUT()
        verify { lightOrganStateMachine.start() }
    }

    @Test
    fun `the light organ does not start automatically when start automatically is false`() {
        every { startAutomatically.value } returns false
        createSUT()
        verify(exactly = 0) { lightOrganStateMachine.start() }
    }

    @Test
    fun `start the light organ`() {
        val sut = createSUT()
        sut.start()
        verify { lightOrganStateMachine.start() }
    }

    @Test
    fun `stop the light organ`() {
        val sut = createSUT()
        sut.stop()
        verify { lightOrganStateMachine.stop() }
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()
        sut.addSubscriber(newSubscriber)
        verify { lightOrganStateMachine.addSubscriber(newSubscriber) }
    }

}