package gui.dashboard

import config.Config
import config.ConfigPersister
import gui.dashboard.tiles.color.ColorTileViewModel
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel
import gui.tiles.audioInput.AudioInputTileViewModel
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import lightOrgan.LightOrgan
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class DashboardViewModelTests {

    private val lightOrgan: LightOrgan = mockk()
    private val audioInputTileViewModel: AudioInputTileViewModel = mockk()
    private val colorTileViewModel: ColorTileViewModel = mockk(relaxed = true)
    private val spectrumTileViewModel: SpectrumTileViewModel = mockk(relaxed = true)
    private val configPersister: ConfigPersister = mockk(relaxed = true)
    private val config: Config = mockk(relaxed = true)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): DashboardViewModel {
        return DashboardViewModel(
            lightOrgan = lightOrgan,
            audioInputTileViewModel = audioInputTileViewModel,
            colorTileViewModel = colorTileViewModel,
            configPersister = configPersister,
            spectrumTileViewModel = spectrumTileViewModel,
            config = config
        )
    }

    @Test
    fun `the color tile is subscribed to the light organ`() {
        createSUT()
        verify { lightOrgan.addSubscriber(colorTileViewModel) }
    }

    @Test
    fun `changes to the config are persisted`() {
        createSUT()
        verify { configPersister.persist(config) }
    }

}
