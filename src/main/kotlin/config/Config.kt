package config

// TODO: Document the config
data class Config(
    val colorWheel: ColorWheel = ColorWheel(40F, 120F, 0.25F),
    val highPassFilter: HighPassFilter = HighPassFilter(120F, 15F),
    val sampleSize: Int = 4096,
    val interpolatedSampleSize: Int = 65536
)

