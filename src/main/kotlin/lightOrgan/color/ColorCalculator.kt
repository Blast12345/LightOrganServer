package lightOrgan.color

import androidx.compose.ui.graphics.colorspace.ColorSpaces
import bins.FrequencyBin
import bins.FrequencyBins
import color.Light
import color.LightExponentialSmoother
import math.physics.sumSoundPressure
import math.smoothing.PeakSmoother
import music.Tuning
import kotlin.math.pow
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.ui.graphics.Color as ComposeColor

// ENHANCEMENT: Expose smoothing configuration
// ENHANCEMENT: Return combined color and individual colors
// ENHANCEMENT: Enforce sRGB via a type.
// ENHANCEMENT: Gamma correction on clients
// ENHANCEMENT: Equal loudness contours seems increasingly important
// ENHANCEMENT: Expose brightness curve alteration
class ColorCalculator(
    private val tuning: Tuning = Tuning.western(),
    private val colorSmoother: LightExponentialSmoother = LightExponentialSmoother(halfLife = 75.milliseconds),
    private val brightnessSmoother: PeakSmoother = PeakSmoother(halfLife = 1.milliseconds)
) {

    // TODO: Enforce sRGB via a type?
    fun calculate(frequencyBins: FrequencyBins, brightnessMultiplier: Float): List<ComposeColor> {
        val lights = frequencyBins.map { createLight(it) }
        val combinedLight = lights.fold(Light()) { sum, current -> sum + current }

        val combinedSoundPressure =
            sumSoundPressure(frequencyBins.map { it.normalizedSoundPressure * brightnessMultiplier })
        val perceivedLoudness = combinedSoundPressure.pow(0.67f) // TODO: Perceived brightness on bin?
        val brightness = perceivedLoudness

        val smoothedLight = colorSmoother.smooth(combinedLight)
        val smoothedBrightness = brightnessSmoother.smooth(brightness)

        val colorOfCombinedLight = smoothedLight.color.convert(ColorSpaces.Srgb)
        val hueAndSaturation = hueAndSaturation(colorOfCombinedLight)

        if (hueAndSaturation.second > 0.1f) {
            println("ASDF")
        }
        return listOf(
            ComposeColor.hsv(
                hue = (hueAndSaturation.first * 360f).coerceIn(0f, 360f),
                saturation = hueAndSaturation.second.coerceIn(0f, 1f),
                value = smoothedBrightness.pow(2.0f).coerceIn(0f, 1f),
                colorSpace = ColorSpaces.Srgb
            ),
            ComposeColor.hsv(
                hue = (hueAndSaturation.first * 360f).coerceIn(0f, 360f),
                saturation = hueAndSaturation.second.coerceIn(0f, 1f),
                value = smoothedBrightness.pow(1.25f).coerceIn(0f, 1f),
                colorSpace = ColorSpaces.Srgb
            ),
            ComposeColor.hsv(
                hue = (hueAndSaturation.first * 360f).coerceIn(0f, 360f),
                saturation = hueAndSaturation.second.coerceIn(0f, 1f),
                value = combinedSoundPressure.coerceIn(0f, 1f),
                colorSpace = ColorSpaces.Srgb
            ),
            ComposeColor.hsv(
                hue = (hueAndSaturation.first * 360f).coerceIn(0f, 360f),
                saturation = hueAndSaturation.second.coerceIn(0f, 1f),
                value = smoothedBrightness.coerceIn(0f, 1f),
                colorSpace = ColorSpaces.Srgb
            )
        )
    }

    fun hueAndSaturation(color: ComposeColor): Pair<Float, Float> {
        val r = color.component1()
        val g = color.component2()
        val b = color.component3()
        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        val saturation = if (max == 0f) 0f else delta / max
        val hue = when {
            delta == 0f -> 0f
            max == r -> ((g - b) / delta).mod(6f) / 6f
            max == g -> ((b - r) / delta + 2f) / 6f
            else -> ((r - g) / delta + 4f) / 6f
        }
        return hue to saturation
    }

    private fun createLight(frequencyBin: FrequencyBin): Light {
        // TODO: This is where we choose the color space
        val color = ComposeColor.hsv(
            hue = tuning.getPositionInOctave(frequencyBin.frequency).degrees.toFloat().coerceIn(0f, 360f),
            saturation = 1f,
            // TODO: We should probably use the same scale as our brightness
            value = frequencyBin.normalizedSoundPressure.coerceIn(0f, 1f),
            colorSpace = ColorSpaces.Srgb
        )

        return Light.from(color)
    }

//    fun calculate(frequencyBins: FrequencyBins, brightnessMultiplier: Float): List<ComposeColor> {
//        val lights = frequencyBins.map { createLight(it) }
//        val combinedLight = lights.fold(Light()) { sum, current -> sum + current }
//
//        val combinedSoundPressure =
//            sumSoundPressure(frequencyBins.map { it.normalizedSoundPressure }) * brightnessMultiplier
//        val perceivedLoudness = combinedSoundPressure.pow(0.67f)
//        val brightness = perceivedLoudness
//
//        val smoothedLight = colorSmoother.smooth(combinedLight)
//        val smoothedBrightness = brightnessSmoother.smooth(brightness)
//
//        val colorOfCombinedLight = smoothedLight.color.convert(ColorSpaces.Srgb)
//        val hueAndSaturation = hueAndSaturation(colorOfCombinedLight)
//
//        return ComposeColor.hsv(
//            hue = hueAndSaturation.first * 360f,
//            saturation = hueAndSaturation.second,
//            value = (smoothedBrightness * brightnessMultiplier).coerceIn(0f, 1f),
//            colorSpace = ColorSpaces.Srgb
//        )
//    }

}

