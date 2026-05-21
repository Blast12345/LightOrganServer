package color

sealed interface ColorSpace

// RGB
sealed interface RgbColorSpace : ColorSpace
data object Srgb : RgbColorSpace