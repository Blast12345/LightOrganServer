package sound.notes

import kotlin.math.pow

data class Note(val fundamentalFrequency: Float) {

    fun getFrequency(octave: Int): Float {
        return fundamentalFrequency * 2F.pow(octave)
    }

}