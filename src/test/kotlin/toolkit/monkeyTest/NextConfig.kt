package toolkit.monkeyTest

import config.Config
import config.children.Client
import gui.dashboard.tiles.spectrum.SpectrumGuiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.spectrum.SpectrumConfig
import kotlin.random.Random

fun nextConfig(
    clients: Set<Client> = nextClients()
): Config {
    return Config(
        startAutomatically = MutableStateFlow(Random.nextBoolean()),
        clients = clients,
        spectrum = SpectrumConfig(
            frameDuration = nextDuration(),
            approximateBinSpacing = nextPositiveFloat(),
            rolloffThreshold = nextPositiveFloat(),
            highPassFilter = nextHighPassConfig(),
            lowPassFilter = nextLowPassConfig(),
        ),
        spectrumGui = SpectrumGuiConfig(
            scale = Random.nextFloat(),
            lowestFrequency = Random.nextFloat(),
            highestFrequency = Random.nextFloat(),
        ),
        brightnessMultiplier = Random.nextFloat()
    )
}