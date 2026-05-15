package lightOrgan.color

import dsp.peakExtraction.SpectralPeak
import dsp.peakExtraction.SpectralPeaks
import math.geometry.Angle
import math.normalization.UnitInterval
import math.perception.StevensPowerLaw
import math.physics.Light
import math.physics.sumSoundPressure
import music.TuningSystem
import music.WesternTuningSystem
import kotlin.math.abs
import kotlin.math.pow

class ColorWheelAlgorithm(
    private val tuning: TuningSystem = WesternTuningSystem(),
) : ColorAlgorithm {

    override fun calculate(spectralPeaks: SpectralPeaks): SrgbColor {
        val lights = spectralPeaks.map { createLight(it) }
        val combinedLight = lights.fold(Light()) { sum, current -> sum + current }

        val combinedSoundPressure = sumSoundPressure(spectralPeaks.map { it.normalizedSoundPressure })
        val perceivedLoudness = combinedSoundPressure.pow(0.67)
        val perceivedBrightness = perceivedLoudness

        return combinedLight.toSrgbColor(perceivedBrightness)
    }

    fun Light.toSrgbColor(perceivedBrightness: Double): SrgbColor {
        val peak = maxOf(red, green, blue)

        if (peak == 0.0) {
            return SrgbColor(UnitInterval(0.0), UnitInterval(0.0), UnitInterval(0.0))
        }

        val clampedBrightness = perceivedBrightness.coerceAtMost(1.0)
        val scale = clampedBrightness / peak

        return SrgbColor(
            red = UnitInterval.clamped(red * scale),
            green = UnitInterval.clamped(green * scale),
            blue = UnitInterval.clamped(blue * scale)
        )
    }

    private fun createLight(spectralPeak: SpectralPeak): Light {
        val objectiveLoudness = spectralPeak.normalizedSoundPressure
        val subjectiveLoudness = objectiveLoudness.pow(StevensPowerLaw.LOUDNESS_3KHZ_TONE.exponent)
        val desiredSubjectiveBrightness = subjectiveLoudness // We want it to look as bright as it sounds loud
        val objectiveBrightness = desiredSubjectiveBrightness.pow(1.0 / StevensPowerLaw.BRIGHTNESS_5DEG_IN_DARK.exponent)

        val hue = tuning.getPositionInOctave(spectralPeak.frequency)

        return hsbToLight(hue, objectiveBrightness)
    }

    private fun hsbToLight(hue: Angle, brightness: Double): Light {
        val hueSegment = hue.normalized.degrees / 60.0
        val intermediate = brightness * (1.0 - abs(hueSegment.mod(2.0) - 1.0))

        val (red, green, blue) = when {
            hueSegment < 1 -> Triple(brightness, intermediate, 0.0)
            hueSegment < 2 -> Triple(intermediate, brightness, 0.0)
            hueSegment < 3 -> Triple(0.0, brightness, intermediate)
            hueSegment < 4 -> Triple(0.0, intermediate, brightness)
            hueSegment < 5 -> Triple(intermediate, 0.0, brightness)
            else -> Triple(brightness, 0.0, intermediate)
        }

        return Light(red, green, blue)
    }

    /**
     * Magnitude is analogous to sound pressure (not to be confused with SPL, which is a log scale).
     * Because digital audio has a finite range, it's akin to saying with we have a finite pressure range (e.g. 0-100 pascals).
     * Because it's a finite range, we can normalize it to a value from 0 to 1.
     * So, we can say that a magnitude of 0.1 is 10 pascals.
     *
     * Though this variable doesn't functionally do anything, it expresses the relationship.
     */
    private val SpectralPeak.normalizedSoundPressure: Double get() = magnitude.toDouble()

}