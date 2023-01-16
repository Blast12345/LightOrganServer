package config

// TODO: Document the config
interface Config {
    val colorWheel: ColorWheel
    val highPassFilter: HighPassFilter
    val sampleSize: Int
    val interpolatedSampleSize: Int
    val magnitudeEstimationStrategy: MagnitudeEstimationStrategy
}