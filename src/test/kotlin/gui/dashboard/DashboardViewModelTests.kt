package gui.dashboard

import config.Config
import config.ConfigPersister
import gui.dashboard.tiles.color.ColorTileViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganTileViewModel
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class DashboardViewModelTests {

    private val lightOrganTileViewModel: LightOrganTileViewModel = mockk(relaxed = true)
    private val colorTileViewModel: ColorTileViewModel = mockk(relaxed = true)
    private val configPersister: ConfigPersister = mockk(relaxed = true)
    private val config: Config = mockk(relaxed = true)

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DashboardViewModel {
        return DashboardViewModel(
            lightOrganTileViewModel = lightOrganTileViewModel,
            colorTileViewModel = colorTileViewModel,
            configPersister = configPersister,
            config = config
        )
    }

    @Test
    fun `the color tile is subscribed to the light organ`() {
        val sut = createSUT()
        verify { lightOrganTileViewModel.addSubscriber(colorTileViewModel) }
    }

    @Test
    fun `changes to the config are persisted`() {
        val sut = createSUT()
        verify { configPersister.persist(config) }
    }

}