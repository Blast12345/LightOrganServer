package music

import math.geometry.Angle
import math.normalization.normalizeLogarithmically
import kotlin.math.pow

abstract class Tuning {

    abstract val octaveRatio: Float
    abstract val pitchClasses: List<PitchClass>
    abstract val referencePitchClass: PitchClass
    abstract val referenceOctave: Int
    abstract val referenceFrequency: Float

    private val originFrequency: Float by lazy {
        val positionInSystem = referenceOctave + referencePitchClass.positionInOctave
        referenceFrequency / octaveRatio.pow(positionInSystem)
    }

    fun getFrequency(pitchClass: PitchClass, octave: Int): Float {
        val octaveStartFrequency = originFrequency * octaveRatio.pow(octave)
        return octaveStartFrequency * octaveRatio.pow(pitchClass.positionInOctave)
    }

    // Reference: https://www.researchgate.net/figure/The-helical-model-of-pitch-Musical-pitch-is-depicted-as-varying-along-both-a-linear_fig1_272318954
    fun getHelicalAngle(frequency: Float): Angle {
        val value = frequency.normalizeLogarithmically(
            minimum = originFrequency,
            maximum = originFrequency * octaveRatio,
            base = 2F
        )
        return Angle.fromTurns(value.toDouble())
    }

    fun getOctave(frequency: Float): Int {
        return getHelicalAngle(frequency).turns.toInt()
    }

    fun getPositionInOctave(frequency: Float): Angle {
        val degrees = getHelicalAngle(frequency).degrees.mod(360.0)
        return Angle.fromDegrees(degrees)
    }

}