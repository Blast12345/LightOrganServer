package lightOrgan.color

import androidx.compose.ui.graphics.colorspace.ColorSpaces
import color.Light
import color.LightExponentialSmoother
import dsp.bins.FrequencyBin
import dsp.bins.FrequencyBins
import math.physics.sumSoundPressure
import math.smoothing.PeakSmoother
import music.TuningSystem
import music.WesternTuningSystem
import kotlin.math.pow
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.ui.graphics.Color as ComposeColor

// ENHANCEMENT: Expose smoothing configuration
// ENHANCEMENT: Return combined color and individual colors
// ENHANCEMENT: Expose brightness curve alteration
// ENHANCEMENT: Support high gamut color spaces (ideally CIE XYZ) and compress for each output device
class ColorCalculator(
    private val gammaAdjustment: Float = 1f, // e.g. 1.5f, 1.25f, 1f
    private val tuning: TuningSystem = WesternTuningSystem(),
    private val lightSmoother: LightExponentialSmoother = LightExponentialSmoother(halfLife = 75.milliseconds),
    private val brightnessSmoother: PeakSmoother = PeakSmoother(halfLife = 1.milliseconds)
) {

    // TODO: Enforce sRGB via a type?
    fun calculate(frequencyBins: FrequencyBins, brightnessMultiplier: Float): ComposeColor {
        val lights = frequencyBins.map { createLight(it) }
        val combinedLight = lights.fold(Light()) { sum, current -> sum + current }

        val combinedSoundPressure =
            sumSoundPressure(frequencyBins.map { it.normalizedSoundPressure * brightnessMultiplier })
        val perceivedLoudness = combinedSoundPressure.pow(0.67f) // TODO: Perceived brightness on bin?
        val brightness = perceivedLoudness

        val smoothedLight = lightSmoother.smooth(combinedLight)
        val smoothedBrightness = brightnessSmoother.smooth(brightness)

        val hue = smoothedLight.hue.coerceIn(0f, 360f)
        val saturation = smoothedLight.saturation.coerceIn(0f, 1f)
        val value = smoothedBrightness.pow(gammaAdjustment).coerceIn(0f, 1f)

        return ComposeColor.hsv(hue, saturation, value, colorSpace = ColorSpaces.Srgb)
    }

    // TODO: This is where we choose the color space
    private fun createLight(frequencyBin: FrequencyBin): Light {
        val perceptionScale = 2.0f
        val soundPressure = frequencyBin.normalizedSoundPressure.coerceIn(0f, 1f)

        val color = ComposeColor.hsv(
            hue = tuning.getPositionInOctave(frequencyBin.frequency).degrees.toFloat().coerceIn(0f, 360f),
            saturation = 1f,
            value = soundPressure.pow(perceptionScale),
            colorSpace = ColorSpaces.LinearSrgb
        )

        return Light(color.red, color.green, color.blue)
    }

}

