package gui.dashboard

import gui.dashboard.tiles.lightOrgan.LightOrganTileViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganTileViewModelFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import lightOrgan.LightOrganStateMachine
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DashboardViewModelFactoryTests {

    private val lightOrganTileViewModelFactory: LightOrganTileViewModelFactory = mockk()

    private val lightOrganStateMachine: LightOrganStateMachine = mockk()

    private val lightOrganTileViewModel: LightOrganTileViewModel = mockk()

    @BeforeEach
    fun setupHappyPath() {
        every { lightOrganTileViewModelFactory.create(lightOrganStateMachine) } returns lightOrganTileViewModel
        every { lightOrganTileViewModel.addSubscriber(any()) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): DashboardViewModelFactory {
        return DashboardViewModelFactory(
            lightOrganTileViewModelFactory = lightOrganTileViewModelFactory
        )
    }

    @Test
    fun `contains an element that controls the light organ`() {
        val sut = createSUT()
        val viewModel = sut.create(lightOrganStateMachine)
        assertEquals(lightOrganTileViewModel, viewModel.lightOrganTileViewModel)
    }

}
