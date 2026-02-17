package gui

import gui.tiles.audioInput.AudioInputTileViewModel
import gui.tiles.color.ColorTileViewModel
import gui.tiles.gateway.GatewayTileViewModel
import gui.tiles.spectrum.SpectrumTileViewModel
import lightOrgan.LightOrgan

// TODO: Do I need to test this? Does it even need to exist?
class DashboardViewModel(
    val audioInputTileViewModel: AudioInputTileViewModel,
    val spectrumTileViewModel: SpectrumTileViewModel,
    val colorTileViewModel: ColorTileViewModel,
    val gatewayTileViewModel: GatewayTileViewModel
) {

    companion object {
        fun create(
            lightOrgan: LightOrgan,
            snackbarController: SnackbarController
        ): DashboardViewModel {
            return DashboardViewModel(
                audioInputTileViewModel = AudioInputTileViewModel(lightOrgan.audioInputManager, snackbarController),
                spectrumTileViewModel = SpectrumTileViewModel(lightOrgan.soundProcessor),
                colorTileViewModel = ColorTileViewModel(lightOrgan.colorProcessor),
                gatewayTileViewModel = GatewayTileViewModel(lightOrgan.gatewayManager, snackbarController)
            )
        }
    }

}
