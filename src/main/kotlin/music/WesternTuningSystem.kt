package music

@Suppress("PropertyName")
class WesternTuningSystem(a4Frequency: Float = 440f) : TuningSystem() {

    private val semitonesPerOctave = 12

    val C = pitchClass("C", 0)
    val C_SHARP = pitchClass("C#", 1)
    val D = pitchClass("D", 2)
    val D_SHARP = pitchClass("D#", 3)
    val E = pitchClass("E", 4)
    val F = pitchClass("F", 5)
    val F_SHARP = pitchClass("F#", 6)
    val G = pitchClass("G", 7)
    val G_SHARP = pitchClass("G#", 8)
    val A = pitchClass("A", 9)
    val A_SHARP = pitchClass("A#", 10)
    val B = pitchClass("B", 11)

    override val pitchClasses = listOf(C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B)
    override val octaveRatio: Float = 2f
    override val referencePitchClass: PitchClass = A
    override val referenceOctave: Int = 4
    override val referenceFrequency: Float = a4Frequency

    private fun pitchClass(name: String, semitonesFromC: Int): PitchClass {
        return PitchClass(name, semitonesFromC.toFloat() / semitonesPerOctave)
    }

}