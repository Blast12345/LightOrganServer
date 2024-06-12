package toolkit.monkeyTest

import config.Config
import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import sound.bins.frequency.filters.Crossover
import kotlin.random.Random

fun nextConfig(
    clients: Set<Client> = nextClients(),
    hueSampleSize: Int = nextPositiveInt(),
    hueLowCrossover: Crossover = nextCrossover(),
    hueHighCrossover: Crossover = nextCrossover(),
    brightnessSampleSize: Int = nextPositiveInt(),
    brightnessLowCrossover: Crossover = nextCrossover(),
    brightnessHighCrossover: Crossover = nextCrossover(),
    interpolatedSampleSize: Int = nextPositiveInt(),
): Config {
    return Config(
        startAutomatically = MutableStateFlow(Random.nextBoolean()),
        clients = clients,
        hueSampleSize = hueSampleSize,
        hueLowCrossover = hueLowCrossover,
        hueHighCrossover = hueHighCrossover,
        brightnessSampleSize = brightnessSampleSize,
        brightnessLowCrossover = brightnessLowCrossover,
        brightnessHighCrossover = brightnessHighCrossover,
        interpolatedSampleSize = interpolatedSampleSize,
        brightnessMultiplier = Random.nextFloat(),
        millisecondsToWaitBetweenCheckingForNewAudio = nextPositiveLong(),
    )
}
