package gui.dashboard

import config.Config
import config.ConfigPersister
import gui.dashboard.tiles.color.ColorTileViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganTileViewModel
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach

class DashboardViewModelTests {

    private val lightOrganTileViewModel: LightOrganTileViewModel = mockk(relaxed = true)
    private val colorTileViewModel: ColorTileViewModel = mockk(relaxed = true)
    private val configPersister: ConfigPersister = mockk(relaxed = true)
    private val config: Config = mockk(relaxed = true)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    // TODO:
//    private fun createSUT(): DashboardViewModel {
//        return DashboardViewModel(
//            lightOrganTileViewModel = lightOrganTileViewModel,
//            colorTileViewModel = colorTileViewModel,
//            configPersister = configPersister,
//            config = config
//        )
//    }
//
//    @Test
//    fun `the color tile is subscribed to the light organ`() {
//        createSUT()
//        verify { lightOrganTileViewModel.addSubscriber(colorTileViewModel) }
//    }
//
//    @Test
//    fun `changes to the config are persisted`() {
//        createSUT()
//        verify { configPersister.persist(config) }
//    }

}
