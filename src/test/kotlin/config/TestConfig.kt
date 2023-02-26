package config

class TestConfig : Config(
    clients = listOf(),
    colorWheel = ColorWheel(40F, 120F, 0.25F),
    highPassFilter = HighPassFilter(120F, 15F),
    sampleSize = 4800,
    interpolatedSampleSize = 48000,
    magnitudeEstimationStrategy = MagnitudeEstimationStrategy(5),
    magnitudeMultiplier = 1F,
    millisecondsToWaitBetweenCheckingForNewAudio = 1
)