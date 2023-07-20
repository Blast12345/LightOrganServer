package gui.dashboard

import gui.dashboard.tiles.color.ColorViewModel
import gui.dashboard.tiles.lightOrgan.LightOrganViewModel

class DashboardViewModel(
    val lightOrganViewModel: LightOrganViewModel,
    val colorViewModel: ColorViewModel
) {

    init {
        lightOrganViewModel.addSubscriber(colorViewModel)
    }

}