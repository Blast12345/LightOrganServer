package config

import config.children.Client
import dsp.bins.frequency.filters.Crossover
import dsp.bins.notes.Notes
import kotlinx.coroutines.flow.MutableStateFlow

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            lowCrossover = Crossover(
                stopFrequency = Notes.C.getFrequency(octave = 0),
                cornerFrequency = Notes.C.getFrequency(octave = 1)
            ),
            highCrossover = Crossover(
                cornerFrequency = Notes.C.getFrequency(octave = 2),
                stopFrequency = Notes.C.getFrequency(octave = 3)
            ),
            sampleSize = 3600, // now relative to sample rate because of mixdown
            interpolatedSampleSize = 65536,
            spectrumMultiplier = 4F,
        )
    }

}
