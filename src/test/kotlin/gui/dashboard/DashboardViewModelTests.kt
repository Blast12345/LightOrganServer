package gui.dashboard

import LightOrganStateMachine
import gui.dashboard.tiles.SynestheticViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.Server
import kotlin.random.Random

class DashboardViewModelTests {

    private val lightOrganStateMachine: LightOrganStateMachine = mockk()
    private val server: Server = mockk()
    private val synestheticViewModel: SynestheticViewModel = mockk()

    @BeforeEach
    fun setup() {
        every { lightOrganStateMachine.addSubscriber(any()) } returns Unit
        every { synestheticViewModel.startAutomatically.value } returns Random.nextBoolean()
        every { synestheticViewModel.start() } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DashboardViewModel {
        return DashboardViewModel(
            lightOrganStateMachine = lightOrganStateMachine,
            server = server,
            synestheticViewModel = synestheticViewModel
        )
    }

    @Test
    fun `the server is subscribed to the light organ at initialization`() {
        createSUT()
        verify { lightOrganStateMachine.addSubscriber(server) }
    }

    @Test
    fun `the light organ starts automatically when the start automatically is true`() {
        every { synestheticViewModel.startAutomatically.value } returns true
        createSUT()
        verify { synestheticViewModel.start() }
    }

    @Test
    fun `the light organ does not start automatically when start automatically is false`() {
        every { synestheticViewModel.startAutomatically.value } returns false
        createSUT()
        verify(exactly = 0) { synestheticViewModel.start() }
    }

}