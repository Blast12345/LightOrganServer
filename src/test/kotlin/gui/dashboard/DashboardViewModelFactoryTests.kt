package gui.dashboard

import LightOrganStateMachine
import gui.dashboard.tiles.LightOrganViewModel
import gui.dashboard.tiles.LightOrganViewModelFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DashboardViewModelFactoryTests {

    private val lightOrganViewModelFactory: LightOrganViewModelFactory = mockk()

    private val lightOrganStateMachine: LightOrganStateMachine = mockk()

    private val lightOrganViewModel: LightOrganViewModel = mockk()

    @BeforeEach
    fun setup() {
        every { lightOrganViewModelFactory.create(lightOrganStateMachine) } returns lightOrganViewModel
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DashboardViewModelFactory {
        return DashboardViewModelFactory(
            lightOrganViewModelFactory = lightOrganViewModelFactory
        )
    }

    @Test
    fun `contains an element that controls the light organ`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine)
        assertEquals(lightOrganViewModel, viewModel.lightOrganViewModel)
    }

}