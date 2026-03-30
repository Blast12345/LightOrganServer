package gui.dashboard

import config.Config
import config.ConfigPersister
import config.ConfigSingleton
import gui.dashboard.tiles.color.ColorTileViewModel
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel
import gui.tiles.audioInput.AudioInputTileViewModel
import lightOrgan.LightOrgan

class DashboardViewModel(
    lightOrgan: LightOrgan,
    snackbarController: SnackbarController,
    private val configPersister: ConfigPersister = ConfigPersister(),
    private val config: Config = ConfigSingleton
) {

    val audioInputTileViewModel = AudioInputTileViewModel(lightOrgan.inputManager, snackbarController)
    val spectrumTileViewModel = SpectrumTileViewModel(lightOrgan.spectrumManager)
    val colorTileViewModel = ColorTileViewModel(lightOrgan.colorManager)

    init {
        startPersistingConfigChanges()
    }

    private fun startPersistingConfigChanges() {
        configPersister.persist(config)
    }

}