package gui.dashboard

import LightOrganStateMachine
import gui.dashboard.tiles.SynestheticViewModel
import gui.dashboard.tiles.SynestheticViewModelFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DashboardViewModelFactoryTests {

    private val synestheticViewModelFactory: SynestheticViewModelFactory = mockk()

    private val lightOrganStateMachine: LightOrganStateMachine = mockk()

    private val synestheticViewModel: SynestheticViewModel = mockk()

    @BeforeEach
    fun setup() {
        every { synestheticViewModelFactory.create(lightOrganStateMachine) } returns synestheticViewModel
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DashboardViewModelFactory {
        return DashboardViewModelFactory(
            synestheticViewModelFactory = synestheticViewModelFactory
        )
    }

    @Test
    fun `contains an element that controls the light organ`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine)
        assertEquals(synestheticViewModel, viewModel.synestheticViewModel)
    }

}