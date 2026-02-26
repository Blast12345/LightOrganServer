package gui.dashboard

import gui.dashboard.tiles.color.ColorTileViewModelFactory
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel
import gui.tiles.audioInput.AudioInputTileViewModel
import lightOrgan.LightOrgan
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager

class DashboardViewModelFactory(
    private val colorTileViewModelFactory: ColorTileViewModelFactory = ColorTileViewModelFactory()
) {

    fun create(
        audioInputManager: AudioInputManager,
        spectrumManager: SpectrumManager,
        lightOrgan: LightOrgan,
        snackbarController: SnackbarController
    ): DashboardViewModel {
        return DashboardViewModel(
            lightOrgan = lightOrgan,
            audioInputTileViewModel = AudioInputTileViewModel(audioInputManager, snackbarController),
            spectrumTileViewModel = SpectrumTileViewModel(spectrumManager),
            colorTileViewModel = colorTileViewModelFactory.create()
        )
    }

}
