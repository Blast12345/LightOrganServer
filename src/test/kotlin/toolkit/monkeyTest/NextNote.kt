package toolkit.monkeyTest

import sound.notes.Note
import kotlin.random.Random

fun nextNote(): Note {
    return Note(
        rootFrequency = Random.nextFloat()
    )
}
