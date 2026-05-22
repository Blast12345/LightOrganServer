package color

sealed interface ColorSpace

// RGB
sealed interface RgbColorSpace : ColorSpace
data object StandardRGB : RgbColorSpace
data object LinearRGB : RgbColorSpace