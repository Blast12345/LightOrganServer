package lightOrgan.color

import color.*
import dsp.peakExtraction.SpectralPeak
import dsp.peakExtraction.SpectralPeaks
import dsp.peakExtraction.combinedMagnitude
import math.geometry.Angle
import math.normalization.UnitInterval
import math.perception.StevensPowerLaw
import math.physics.Light
import music.TuningSystem
import music.WesternTuningSystem

class ColorWheelAlgorithm(
    private val tuning: TuningSystem = WesternTuningSystem(),
) : ColorAlgorithm {

    override fun calculate(spectralPeaks: SpectralPeaks): SrgbColor {
        // think of each peak as its own light source
        val lights = spectralPeaks.map { createLight(it) }

        // we point the lights at a virtual wall and mix them together
        val combinedLight = lights.fold(Light()) { sum, current -> sum + current }

        // we can calculate the chromaticity - color, independent of brightness
        val combinedChromaticity = combinedLight.chromaticity

        // we then calculate the overall loudness of the sound
        val subjectiveLoudness = StevensPowerLaw.LOUDNESS_3KHZ_TONE.perceivedIntensity(spectralPeaks.combinedMagnitude)
        val brightness = UnitInterval.clamped(subjectiveLoudness)

        // finally, we create a color using the hue and saturation of the combined light
        // and make it as bright as the sound is loud
        return when (combinedChromaticity) {
            is Chromaticity.Chromatic -> HsbColor<Srgb>(combinedChromaticity.hue, combinedChromaticity.saturation, brightness).toRgb()
            is Chromaticity.Achromatic -> HsbColor<Srgb>(Angle.zero, UnitInterval.zero, brightness).toRgb()
            null -> SrgbColors.Black
        }
    }

    private fun createLight(spectralPeak: SpectralPeak): Light {
        val rawColor = HsbColor<Srgb>(
            hue = tuning.getPositionInOctave(spectralPeak.frequency),
            saturation = UnitInterval.one,
            brightness = UnitInterval.one
        ).toRgb().toLinear()

        val subjectiveLoudness = StevensPowerLaw.LOUDNESS_3KHZ_TONE.perceivedIntensity(spectralPeak.magnitude.toDouble())
        val objectiveBrightness = StevensPowerLaw.BRIGHTNESS_5DEG_IN_DARK.stimulusIntensity(subjectiveLoudness)

        return Light(
            rawColor.red.value * objectiveBrightness,
            rawColor.green.value * objectiveBrightness,
            rawColor.blue.value * objectiveBrightness,
        )
    }

}