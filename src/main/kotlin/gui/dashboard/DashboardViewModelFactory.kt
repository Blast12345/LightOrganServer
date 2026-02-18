package gui.dashboard

import gui.dashboard.tiles.color.ColorTileViewModelFactory
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel
import gui.tiles.audioInput.AudioInputTileViewModel
import lightOrgan.LightOrgan

class DashboardViewModelFactory(
    private val colorTileViewModelFactory: ColorTileViewModelFactory = ColorTileViewModelFactory(),
    private val spectrumTileViewModel: SpectrumTileViewModel = SpectrumTileViewModel()
) {

    fun create(
        lightOrgan: LightOrgan,
        snackbarController: SnackbarController
    ): DashboardViewModel {
        return DashboardViewModel(
            lightOrgan = lightOrgan,
            audioInputTileViewModel = AudioInputTileViewModel(lightOrgan.audioInputManager, snackbarController),
            colorTileViewModel = colorTileViewModelFactory.create(),
            spectrumTileViewModel = spectrumTileViewModel
        )
    }

}
