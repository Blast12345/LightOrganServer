package gui.dashboard

import config.Config
import config.ConfigPersister
import config.ConfigSingleton
import gui.dashboard.tiles.color.ColorViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganViewModel

class DashboardViewModel(
    val lightOrganViewModel: LightOrganViewModel,
    val colorViewModel: ColorViewModel,
    private val configPersister: ConfigPersister = ConfigPersister(),
    private val config: Config = ConfigSingleton
) {

    init {
        subscribeColorTileToTheLightOrgan()
        startPersistingConfigChanges()
    }

    private fun subscribeColorTileToTheLightOrgan() {
        lightOrganViewModel.addSubscriber(colorViewModel)
    }

    private fun startPersistingConfigChanges() {
        configPersister.persist(config)
    }

}