package gui.dashboard

import config.Config
import config.ConfigPersister
import config.ConfigSingleton
import gui.dashboard.tiles.color.ColorTileViewModel
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel
import gui.tiles.audioInput.AudioInputTileViewModel
import lightOrgan.LightOrgan

class DashboardViewModel(
    val lightOrgan: LightOrgan,
    val audioInputTileViewModel: AudioInputTileViewModel,
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
        lightOrgan.addSubscriber(colorTileViewModel)
    }

    private fun startPersistingConfigChanges() {
        configPersister.persist(config)
    }

}