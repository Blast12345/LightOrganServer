package gui.dashboard

import gui.dashboard.tiles.color.ColorTileViewModelFactory
import gui.dashboard.tiles.lightOrgan.LightOrganTileViewModelFactory
import lightOrgan.LightOrganStateMachine

class DashboardViewModelFactory(
    private val lightOrganTileViewModelFactory: LightOrganTileViewModelFactory = LightOrganTileViewModelFactory(),
    private val colorTileViewModelFactory: ColorTileViewModelFactory = ColorTileViewModelFactory()
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): DashboardViewModel {
        return DashboardViewModel(
            lightOrganTileViewModel = lightOrganTileViewModelFactory.create(lightOrganStateMachine),
            colorTileViewModel = colorTileViewModelFactory.create()
        )
    }

}