package color.stevensPowerLaw

import config.ConfigSingleton
import math.LogarithmicScale

val NoteScale = LogarithmicScale(
    minimum = ConfigSingleton.rootNote.getFrequency(1),
    maximum = ConfigSingleton.rootNote.getFrequency(2),
    power = 2F
)