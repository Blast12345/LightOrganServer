package config

var ConfigSingleton = Config(
    clients = listOf(Client("192.168.1.55")),
    colorWheel = ColorWheel(40F, 120F, 0.25F),
    highPassFilter = HighPassFilter(120F, 15F),
    sampleSize = 4100,
    interpolatedSampleSize = 65536,
    magnitudeEstimationStrategy = MagnitudeEstimationStrategy(5),
    magnitudeMultiplier = 1.25F,
    millisecondsToWaitBetweenCheckingForNewAudio = 1
)