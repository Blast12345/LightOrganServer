package music

import kotlin.math.pow

// Key.C.octave(1) -> Note.frequency

// TODO: Should this be private? Should it be called something else?
data class Note(val fundamentalFrequency: Float) {

    fun getFrequency(octave: Int): Float {
        return fundamentalFrequency * 2F.pow(octave)
    }

}

// TODO: Western notes? Can the object take params for tuning? Also... are these only useful for tests now?
object Keys {

    // I'm just assuming A4=440 Hz tuning for simplicity's sake.
    // Reference: https://pages.mtu.edu/~suits/notefreqs.html
    private const val notesInAnOctave = 12F
    private const val tuningOctave = 4
    private const val tuning = 440F
    private val tuningFundamental = tuning * 2F.pow(-tuningOctave)

    val C = createNote(positionRelativeToA = -9)
    val C_SHARP = createNote(positionRelativeToA = -8)
    val D = createNote(positionRelativeToA = -7)
    val D_SHARP = createNote(positionRelativeToA = -6)
    val E = createNote(positionRelativeToA = -5)
    val F = createNote(positionRelativeToA = -4)
    val F_SHARP = createNote(positionRelativeToA = -3)
    val G = createNote(positionRelativeToA = -2)
    val G_SHARP = createNote(positionRelativeToA = -1)
    val A = createNote(positionRelativeToA = 0)
    val A_SHARP = createNote(positionRelativeToA = 1)
    val B = createNote(positionRelativeToA = 2)

    val all = listOf(C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B)

    private fun createNote(positionRelativeToA: Int): Note {
        return Note(
            fundamentalFrequency = tuningFundamental * 2F.pow(positionRelativeToA / notesInAnOctave)
        )
    }

}
