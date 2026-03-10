package config

import bins.HighPassFilter
import bins.LowPassFilter
import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import music.Keys

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            highPassFilter = HighPassFilter(
                frequency = Keys.C.getFrequency(octave = 1),
                slope = 24f
            ),
            lowPassFilter = LowPassFilter(
                frequency = Keys.G.getFrequency(octave = 2),
                slope = 48f
            ),
            sampleSize = 2400, // now relative to sample rate because of mixdown
            interpolatedSampleSize = 65536, //32768, //65536,
            magnitudeMultiplier = 4F,
        )
    }

}
