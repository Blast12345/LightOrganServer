package config

import config.children.Client
import dsp.filtering.config.FilterConfig
import kotlinx.coroutines.flow.MutableStateFlow
import sound.notes.Notes

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            highPassFilter = FilterConfig.butterworthHighPassFromSlope(
                frequency = Notes.C.getFrequency(octave = 1) - 10,
                dbPerOctave = 48
            ),
            lowPassFilter = FilterConfig.butterworthLowPassFromSlope(
                frequency = Notes.G.getFrequency(octave = 2),
                dbPerOctave = 48
            ),
            sampleSize = 3000, // now relative to sample rate because of mixdown
            interpolatedSampleSize = 65536,
            magnitudeMultiplier = 4F,
        )
    }

}
