package toolkit.monkeyTest

import config.Config
import config.children.Client
import gui.dashboard.tiles.spectrum.SpectrumGuiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.spectrum.SpectrumConfig
import kotlin.random.Random

fun nextConfig(
    clients: Set<Client> = nextClients(),
    sampleSize: Int = nextPositiveInt(),
    interpolatedSampleSize: Int = nextPositiveInt(),
    brightnessMultiplier: Float = Random.nextFloat()
): Config {
    return Config(
        startAutomatically = MutableStateFlow(Random.nextBoolean()),
        clients = clients,
        spectrum = SpectrumConfig(
            sampleSize = sampleSize,
            interpolatedSampleSize = interpolatedSampleSize,
            highPassFilter = null,
            lowPassFilter = null,
        ),
        spectrumGui = SpectrumGuiConfig(
            scale = Random.nextFloat(),
            lowestFrequency = Random.nextFloat(),
            highestFrequency = Random.nextFloat(),
        ),
        brightnessMultiplier = brightnessMultiplier
    )
}