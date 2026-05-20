package color

import math.geometry.Angle

data class RgbRatios(val red: Double, val green: Double, val blue: Double) {

    companion object {
        fun fromHue(hue: Angle): RgbRatios {
            val sectorSize = 60.0
            val hueValue = hue.degrees.mod(360.0)
            val sector = (hueValue / sectorSize).toInt()
            val fractionalPosition = (hueValue / sectorSize) - sector

            return when (sector) {
                0 -> RgbRatios(red = 1.0, green = fractionalPosition, blue = 0.0)
                1 -> RgbRatios(red = 1.0 - fractionalPosition, green = 1.0, blue = 0.0)
                2 -> RgbRatios(red = 0.0, green = 1.0, blue = fractionalPosition)
                3 -> RgbRatios(red = 0.0, green = 1.0 - fractionalPosition, blue = 1.0)
                4 -> RgbRatios(red = fractionalPosition, green = 0.0, blue = 1.0)
                5 -> RgbRatios(red = 1.0, green = 0.0, blue = 1.0 - fractionalPosition)
                else -> RgbRatios(red = 1.0, green = 0.0, blue = 0.0)
            }
        }
    }
}