package config

import config.children.Client
import sound.bins.frequency.filters.Crossover
import sound.bins.notes.Notes
import kotlinx.coroutines.flow.MutableStateFlow

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            lowCrossover = _root_ide_package_.sound.bins.frequency.filters.Crossover(
                stopFrequency = _root_ide_package_.sound.bins.notes.Notes.C.getFrequency(octave = 0),
                cornerFrequency = _root_ide_package_.sound.bins.notes.Notes.C.getFrequency(octave = 1)
            ),
            highCrossover = _root_ide_package_.sound.bins.frequency.filters.Crossover(
                cornerFrequency = _root_ide_package_.sound.bins.notes.Notes.C.getFrequency(octave = 2),
                stopFrequency = _root_ide_package_.sound.bins.notes.Notes.C.getFrequency(octave = 3)
            ),
            sampleSize = 3600, // now relative to sample rate because of mixdown
            interpolatedSampleSize = 65536,
            spectrumMultiplier = 4F,
        )
    }

}
