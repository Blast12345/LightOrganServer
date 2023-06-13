package gui.dashboard

import gui.dashboard.tiles.color.ColorViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganViewModel
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class DashboardViewModelTests {

    private val lightOrganViewModel: LightOrganViewModel = mockk(relaxed = true)
    private val colorViewModel: ColorViewModel = mockk(relaxed = true)

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DashboardViewModel {
        return DashboardViewModel(
            lightOrganViewModel = lightOrganViewModel,
            colorViewModel = colorViewModel
        )
    }

    @Test
    fun `the color tile is subscribed to the light organ`() {
        val sut = createSUT()
        verify { lightOrganViewModel.addSubscriber(colorViewModel) }
    }

}