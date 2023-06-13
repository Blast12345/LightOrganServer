package gui.dashboard

import LightOrganStateMachine
import gui.dashboard.tiles.color.ColorViewModelFactory
import gui.dashboard.tiles.lightOrgan.LightOrganViewModelFactory

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