package color.stevensPowerLaw

import sound.notes.Notes

// TODO: Test me
val MusicScale = LogarithmicScale(
    minimum = Notes.C.getFrequency(1),
    maximum = Notes.C.getFrequency(2),
    power = 2F
)