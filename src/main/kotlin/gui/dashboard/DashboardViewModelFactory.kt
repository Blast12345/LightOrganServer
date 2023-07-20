package gui.dashboard

import gui.dashboard.tiles.color.ColorViewModelFactory
import gui.dashboard.tiles.lightOrgan.LightOrganViewModelFactory
import lightOrgan.LightOrganStateMachine

class DashboardViewModelFactory(
    private val lightOrganViewModelFactory: LightOrganViewModelFactory = LightOrganViewModelFactory(),
    private val colorViewModelFactory: ColorViewModelFactory = ColorViewModelFactory()
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): DashboardViewModel {
        return DashboardViewModel(
            lightOrganViewModel = lightOrganViewModelFactory.create(lightOrganStateMachine),
            colorViewModel = colorViewModelFactory.create()
        )
    }

}