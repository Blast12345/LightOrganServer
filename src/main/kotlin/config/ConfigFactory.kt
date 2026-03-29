package config

import config.children.Client
import dsp.filtering.config.FilterConfig
import dsp.filtering.config.FilterFamily
import dsp.filtering.config.FilterOrder
import gui.dashboard.tiles.spectrum.SpectrumGuiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.spectrum.SpectrumConfig
import music.Keys

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            spectrum = SpectrumConfig(
                sampleSize = 3000, //28, // 3000
                interpolatedSampleSize = 65536, //512, //65536,
                highPassFilter = FilterConfig.HighPass(
                    family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(48)),
                    frequency = Keys.C.getFrequency(octave = 1) // TODO: How to handle -3 dB?
                ),
                lowPassFilter = FilterConfig.LowPass(
                    family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(48)),
                    frequency = Keys.A.getFrequency(octave = 2)
                ),
            ),
            spectrumGui = SpectrumGuiConfig(
                scale = 3F,
                lowestFrequency = 0f,
                highestFrequency = 160F,
            ),
            brightnessMultiplier = 3F, // TODO: Gain increase instead?
        )
    }

}
