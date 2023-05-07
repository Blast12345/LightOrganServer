package gui.dashboard.tiles.synesthetic

import LightOrganStateMachine
import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class SynestheticViewModelFactoryTests {

    private val lightOrganStateMachine: LightOrganStateMachine = mockk()
    private val config: Config = mockk()

    @BeforeEach
    fun setup() {
        every { lightOrganStateMachine.isRunning } returns Random.nextBoolean()
        every { config.startAutomatically } returns Random.nextBoolean()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): SynestheticViewModelFactory {
        return SynestheticViewModelFactory()
    }

    @Test
    fun `start automatically defaults to the config state`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine, config)
        assertEquals(config.startAutomatically, viewModel.startAutomatically.value)
    }

    @Test
    fun `is running defaults to the light organ state`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine, config)
        assertEquals(lightOrganStateMachine.isRunning, viewModel.isRunning.value)
    }

    @Test
    fun `the state machine is passed to the view model`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine, config)
        assertEquals(lightOrganStateMachine, viewModel.lightOrganStateMachine)
    }

}