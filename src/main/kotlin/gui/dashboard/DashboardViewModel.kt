package gui.dashboard

import config.Config
import config.ConfigPersister
import config.ConfigSingleton
import gateway.GatewayManager
import gui.dashboard.tiles.color.ColorTileViewModel
import gui.dashboard.tiles.color.ColorTileViewModelFactory
import gui.dashboard.tiles.gateway.GatewayTileViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganTileViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganTileViewModelFactory
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel
import lightOrgan.LightOrganStateMachine

class DashboardViewModel(
    val lightOrganTileViewModel: LightOrganTileViewModel,
    val colorTileViewModel: ColorTileViewModel,
    val spectrumTileViewModel: SpectrumTileViewModel,
    val gatewayTileViewModel: GatewayTileViewModel,
    private val configPersister: ConfigPersister = ConfigPersister(),
    private val config: Config = ConfigSingleton
) {

    companion object {

        fun create(
            lightOrganStateMachine: LightOrganStateMachine,
            gatewayManager: GatewayManager,
            lightOrganTileViewModelFactory: LightOrganTileViewModelFactory = LightOrganTileViewModelFactory(),
            colorTileViewModelFactory: ColorTileViewModelFactory = ColorTileViewModelFactory(),
            spectrumTileViewModel: SpectrumTileViewModel = SpectrumTileViewModel()
        ): DashboardViewModel {
            return DashboardViewModel(
                lightOrganTileViewModel = lightOrganTileViewModelFactory.create(lightOrganStateMachine),
                colorTileViewModel = colorTileViewModelFactory.create(),
                spectrumTileViewModel = spectrumTileViewModel,
                gatewayTileViewModel = GatewayTileViewModel.create(gatewayManager)
            )
        }

    }

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
