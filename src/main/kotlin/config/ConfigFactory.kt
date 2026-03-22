package config

import config.children.Client
import dsp.filtering.config.FilterConfig
import dsp.filtering.config.FilterFamily
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
                sampleSize = 3000, // relative to sample rate, mono
                interpolatedSampleSize = 65536,
                highPassFilter = FilterConfig.highPassFromSlope(
                    family = FilterFamily.BUTTERWORTH,
                    frequency = Keys.C.getFrequency(octave = 1) - 10,
                    dbPerOctave = 48
                ),
                lowPassFilter = FilterConfig.lowPassFromSlope(
                    family = FilterFamily.BUTTERWORTH,
                    frequency = Keys.C.getFrequency(octave = 3),
                    dbPerOctave = 48
                ),
            ),
            spectrumGui = SpectrumGuiConfig(
                scale = 3F,
                lowestFrequency = 0f,
                highestFrequency = 160F,
            ),
            brightnessMultiplier = 3F,
        )
    }

}
