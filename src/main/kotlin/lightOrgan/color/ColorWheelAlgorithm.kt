package lightOrgan.color

import color.RgbRatios
import color.StandardRgbColor
import dsp.peakExtraction.SpectralPeak
import dsp.peakExtraction.SpectralPeaks
import math.normalization.UnitInterval
import math.perception.StevensPowerLaw
import math.physics.Light
import math.physics.sumSoundPressure
import music.TuningSystem
import music.WesternTuningSystem
import kotlin.math.pow

class ColorWheelAlgorithm(
    private val tuning: TuningSystem = WesternTuningSystem(),
) : ColorAlgorithm {

    override fun calculate(spectralPeaks: SpectralPeaks): StandardRgbColor {
        // think of each peak as its own light source
        val lights = spectralPeaks.map { createLight(it) }

        // we point the lights at a virtual wall and mix them together
        val combinedLight = lights.fold(Light()) { sum, current -> sum + current }

        // we can calculate the chromaticity - color, independent of brightness
        val combinedChromaticity = combinedLight.chromaticity

        // we then calculate the overall loudness of the sound
        val combinedSoundPressure = sumSoundPressure(spectralPeaks.map { it.soundPressure })
        val subjectiveLoudness = combinedSoundPressure.pow(StevensPowerLaw.LOUDNESS_3KHZ_TONE.exponent)

        // finally, we create a color using the hue and saturation of the combined light
        // and make it as bright as the sound is loud
        return StandardRgbColor.fromHSB(
            hue = combinedChromaticity?.hue,
            saturation = combinedChromaticity?.saturation,
            brightness = UnitInterval.clamped(subjectiveLoudness)
        )
    }

    private fun createLight(spectralPeak: SpectralPeak): Light {
        val hue = tuning.getPositionInOctave(spectralPeak.frequency)
        val hueRatios = RgbRatios.fromHue(hue)

        // we want the light to look as bright as the sound is loud
        val subjectiveLoudness = spectralPeak.soundPressure.pow(StevensPowerLaw.LOUDNESS_3KHZ_TONE.exponent)
        val objectiveBrightness = subjectiveLoudness.pow(1.0 / StevensPowerLaw.BRIGHTNESS_5DEG_IN_DARK.exponent)

        return Light(
            hueRatios.red * objectiveBrightness,
            hueRatios.green * objectiveBrightness,
            hueRatios.blue * objectiveBrightness,
        )
    }

    /**
     * Magnitude is analogous to sound pressure — a linear measure of signal strength.
     * Not to be confused with SPL, which is logarithmic.
     *
     * Though this variable doesn't functionally do anything, it expresses the relationship.
     */
    private val SpectralPeak.soundPressure: Double get() = magnitude.toDouble()

}