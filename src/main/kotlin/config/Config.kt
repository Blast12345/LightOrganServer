package config

open class Config(
    // The clients listening for colors
    var clients: List<Client> = listOf(Client("192.168.1.55")),

    // Helps identify a hue for a given frequency
    var colorWheel: ColorWheel = ColorWheel(40F, 120F, 0.25F),

    // Defines the upper range of bass frequency bins
    var highPassFilter: HighPassFilter = HighPassFilter(120F, 15F),

    // Then number of audio samples to use to identify frequencies
    // Higher values increase accuracy, but also increase latency
    var sampleSize: Int = 4100,

    // We interpolate the sample size to a higher value to reveal obscured frequencies,
    // increasing the accuracy of frequency calculations.
    // Higher values and values that are not powers of 2 increase processing time.
    // TODO: Maybe abandon interpolation in favor of DFTs
    var interpolatedSampleSize: Int = 65536,

    // Helps determine how we estimate the dominant frequency's magnitude
    var magnitudeEstimationStrategy: MagnitudeEstimationStrategy = MagnitudeEstimationStrategy(5),

    // Compensate for low input levels.
    // Setting this "just right" will maximize dynamic range,
    // but setting it too high or too low will decrease dynamic range
    var magnitudeMultiplier: Float = 1.25F,

    // A lower value will increase responsiveness, but increase CPU usage.
    // A higher value will decrease responsiveness, but decrease CPU usage.
    var millisecondsToWaitBetweenCheckingForNewAudio: Long = 1
)