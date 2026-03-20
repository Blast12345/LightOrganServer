package music

import math.geometry.Angle
import math.normalization.normalizeLogarithmically
import kotlin.math.pow

class Tuning private constructor(
    val zeroOctaveStartFrequency: Float,
    val ratio: Float
) {

    companion object {

        fun western(a4Frequency: Float = 440f): Tuning {
            val semitonesPerOctave = 12
            val referenceOctave = 4
            val semitoneOffsetFromOctaveStart = 9

            val totalSemitones = (referenceOctave * semitonesPerOctave) + semitoneOffsetFromOctaveStart
            val zeroOctaveStartFrequency = a4Frequency / 2f.pow(totalSemitones.toFloat() / semitonesPerOctave)

            return Tuning(
                zeroOctaveStartFrequency = zeroOctaveStartFrequency,
                ratio = 2f
            )
        }

    }

    fun getOctave(frequency: Float): Int {
        val helicalAngle = getHelicalAngle(frequency)

        return helicalAngle.turns.toInt()
    }

    fun getPositionInOctave(frequency: Float): Angle {
        val helicalAngle = getHelicalAngle(frequency)
        val degrees = helicalAngle.degrees.mod(360.0)

        return Angle.fromDegrees(degrees)
    }

    // Reference: https://www.researchgate.net/figure/The-helical-model-of-pitch-Musical-pitch-is-depicted-as-varying-along-both-a-linear_fig1_272318954
    // TODO: Test me
    fun getHelicalAngle(frequency: Float): Angle {
        val value = frequency.normalizeLogarithmically(
            minimum = zeroOctaveStartFrequency,
            maximum = zeroOctaveStartFrequency * ratio,
            base = 2F
        )

        return Angle.fromTurns(value.toDouble())
    }

}