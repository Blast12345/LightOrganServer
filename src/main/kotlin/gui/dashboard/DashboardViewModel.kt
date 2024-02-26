package gui.dashboard

import config.Config
import config.ConfigPersister
import config.ConfigSingleton
import gui.dashboard.tiles.color.ColorTileViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganTileViewModel
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel

class DashboardViewModel(
    val lightOrganTileViewModel: LightOrganTileViewModel,
    val colorTileViewModel: ColorTileViewModel,
    val spectrumTileViewModel: SpectrumTileViewModel,
    private val configPersister: ConfigPersister = ConfigPersister(),
    private val config: Config = ConfigSingleton
) {

    init {
        subscribeColorTileToTheLightOrgan()
        startPersistingConfigChanges()
    }

    private fun subscribeColorTileToTheLightOrgan() {
        lightOrganTileViewModel.addSubscriber(colorTileViewModel)
    }

    private fun startPersistingConfigChanges() {
        configPersister.persist(config)
    }

}
