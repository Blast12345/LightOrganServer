package color

import math.featureScaling.denormalize
import math.featureScaling.normalizeLogarithmically
import sound.notes.Notes

class HueCalculator {

    private val rootNote = Notes.C
    private val minimum = 0F
    private val maximum = 1F

    fun calculate(frequency: Float): Float {
        return frequency
            .normalizeToOctave()
            .scaleToHue()
    }

    // Reference: https://en.wikipedia.org/wiki/Octave
    private fun Float.normalizeToOctave(): Float {
        return this.normalizeLogarithmically(
            minimum = rootNote.getFrequency(0),
            maximum = rootNote.getFrequency(1),
            base = 2F
        )
    }

    private fun Float.scaleToHue(): Float {
        return this
            .denormalize(minimum, maximum)
            .loopIntoRange()
    }

    private fun Float.loopIntoRange(): Float {
        return if (this < minimum) {
            (this % 1) + (maximum - minimum)
        } else {
            this % 1
        }
    }

}