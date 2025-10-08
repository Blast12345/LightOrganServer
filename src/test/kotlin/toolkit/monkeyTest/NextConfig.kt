package toolkit.monkeyTest

import config.Config
import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

fun nextConfig(
    sampleSize: Int = nextPositiveInt(),
    interpolatedSampleSize: Int = nextPositiveInt(),
    magnitudeMultiplier: Float = Random.nextFloat()
): Config {
    return Config(
        startAutomatically = MutableStateFlow(Random.nextBoolean()),
        lowCrossover = nextCrossover(),
        highCrossover = nextCrossover(),
        sampleSize = sampleSize,
        interpolatedSampleSize = interpolatedSampleSize,
        magnitudeMultiplier = magnitudeMultiplier,
        millisecondsToWaitBetweenCheckingForNewAudio = nextPositiveLong(),
    )
}
