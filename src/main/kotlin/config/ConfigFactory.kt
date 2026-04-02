package config

import config.children.Client
import dsp.filtering.config.FilterConfig
import dsp.filtering.config.FilterFamily
import dsp.filtering.config.FilterOrder
import dsp.windowing.WindowFunctionType
import gui.dashboard.tiles.spectrum.SpectrumGuiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.spectrum.SpectrumConfig
import music.Keys
import kotlin.time.Duration.Companion.milliseconds

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            spectrum = SpectrumConfig(
                frameDuration = 60.milliseconds,
                approximateBinSpacing = 1f,
                rolloffThreshold = -48f,
                highPassFilter = FilterConfig.HighPass(
                    family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(48)),
                    frequency = Keys.C.getFrequency(octave = 1) // TODO: How to handle -3 dB?
                ),
                lowPassFilter = FilterConfig.LowPass(
                    family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(48)),
                    frequency = Keys.A.getFrequency(octave = 2)
                ),
                windowFunction = WindowFunctionType.BlackmanHarris,
            ),
            spectrumGui = SpectrumGuiConfig(
                scale = 4F,
                lowestFrequency = 0f,
                highestFrequency = 160F,
            ),
            brightnessMultiplier = 3F, // TODO: Gain increase instead?
        )
    }

}
