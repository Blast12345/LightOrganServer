package config

import config.children.Client
import gui.dashboard.tiles.spectrum.SpectrumGuiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.spectrum.SpectrumConfig

@Suppress("LongParameterList")
class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val spectrum: SpectrumConfig,
    val spectrumGui: SpectrumGuiConfig,
    val brightnessMultiplier: Float,
)