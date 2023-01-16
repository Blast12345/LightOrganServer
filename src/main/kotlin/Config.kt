import color.ColorWheel

data class Config(
    val colorWheel: ColorWheel = ColorWheel(40F, 120F, 0F),
    val bassCutoff: Float = 150F
)