package sound.notes

import kotlin.math.pow

data class Note(val rootFrequency: Float) {

    fun getFrequency(octave: Int): Float {
        return rootFrequency * 2F.pow(octave)
    }

}