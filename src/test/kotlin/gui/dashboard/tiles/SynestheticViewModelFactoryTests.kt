package gui.dashboard.tiles

import LightOrganStateMachine
import config.Config
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextConfig

class SynestheticViewModelFactoryTests {

    private val config: Config = nextConfig()
    private val lightOrganStateMachine: LightOrganStateMachine = mockk(relaxed = true)

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): SynestheticViewModelFactory {
        return SynestheticViewModelFactory(
            config = config
        )
    }

    @Test
    fun `start automatically comes from the config`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine)
        assertEquals(config.startAutomatically, viewModel.startAutomatically)
    }

    @Test
    fun `is running reflects the light organ state`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine)
        assertEquals(lightOrganStateMachine.isRunning, viewModel.isRunning)
    }

    @Test
    fun `the light organ state machine is passed through`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine)
        assertEquals(lightOrganStateMachine, viewModel.lightOrganStateMachine)
    }

}