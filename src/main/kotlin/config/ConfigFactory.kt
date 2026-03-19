package config

import config.children.Client
import dsp.filtering.config.FilterConfig
import gui.dashboard.tiles.spectrum.SpectrumGuiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.spectrum.SpectrumConfig
import sound.notes.Notes

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
                highPassFilter = FilterConfig.butterworthHighPassFromSlope(
                    frequency = Notes.C.getFrequency(octave = 1) - 10,
                    dbPerOctave = 48
                ),
                lowPassFilter = FilterConfig.butterworthLowPassFromSlope(
                    frequency = Notes.G.getFrequency(octave = 2),
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
