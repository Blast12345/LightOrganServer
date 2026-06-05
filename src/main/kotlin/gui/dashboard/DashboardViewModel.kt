package gui.dashboard

import config.Config
import config.ConfigPersister
import config.ConfigSingleton
import gui.dashboard.tiles.color.ColorTileViewModel
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel
import gui.snackbar.SnackbarController
import gui.tiles.audioInput.AudioInputTileViewModel
import lightOrgan.color.ColorManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager

class DashboardViewModel(
    inputManager: AudioInputManager,
    spectrumManager: SpectrumManager,
    colorManager: ColorManager,
    snackbarController: SnackbarController,
    private val configPersister: ConfigPersister = ConfigPersister(),
    private val config: Config = ConfigSingleton
) {

    val audioInputTileViewModel = AudioInputTileViewModel(inputManager, snackbarController)
    val spectrumTileViewModel = SpectrumTileViewModel(spectrumManager)
    val colorTileViewModel = ColorTileViewModel(colorManager)

    init {
        startPersistingConfigChanges()
    }

    private fun startPersistingConfigChanges() {
        configPersister.persist(config)
    }

}