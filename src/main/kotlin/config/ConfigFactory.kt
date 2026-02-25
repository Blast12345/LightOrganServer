package config

import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import sound.bins.frequency.filters.Crossover
import sound.notes.Notes

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
