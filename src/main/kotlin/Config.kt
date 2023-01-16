import color.ColorWheel

// TODO: Document the config
data class Config(
    val colorWheel: ColorWheel = ColorWheel(40F, 120F, 0.5F),
    val bassCutoff: Float = 150F,
    val sampleSize: Int = 4096,
    val interpolatedSampleSize: Int = 65536
)