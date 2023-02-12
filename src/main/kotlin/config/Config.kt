package config

import javax.sound.sampled.AudioFormat

interface Config {
    // The audio format of the input
    val audioFormat: AudioFormat

    // The clients listening for colors
    val clients: List<Client>

    // Helps identify a hue for a given frequency
    val colorWheel: ColorWheel

    // Defines the upper range of bass frequency bins
    val highPassFilter: HighPassFilter

    // Then number of audio samples to use to identify frequencies
    // Higher values increase accuracy, but also increase latency
    val sampleSize: Int

    // We interpolate the sample size to a higher value to reveal obscured frequencies,
    // increasing the accuracy of frequency calculations.
    // Higher values and values that are not powers of 2 increase processing time.
    // TODO: Maybe abandon interpolation in favor of DFTs
    val interpolatedSampleSize: Int

    // Helps determine how we estimate the dominant frequency's magnitude
    val magnitudeEstimationStrategy: MagnitudeEstimationStrategy

    // Compensate for low input levels.
    // Setting this "just right" will maximize dynamic range,
    // but setting it too high or too low will decrease dynamic range
    val magnitudeMultiplier: Float

    // A lower value will increase responsiveness, but increase CPU usage.
    // A higher value will decrease responsiveness, but decrease CPU usage.
    val millisecondsToWaitBetweenCheckingForNewAudio: Long

    // A lower value will increase responsiveness, but increase CPU usage.
    // A higher value will decrease responsiveness, but decrease CPU usage.
    val millisecondsToWaitBetweenCheckingForNextColor: Long
}