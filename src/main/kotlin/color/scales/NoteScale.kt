package color.scales

import config.ConfigSingleton
import math.featureScaling.LogarithmicScale

class NoteScale : LogarithmicScale(
    minimum = ConfigSingleton.rootNote.getFrequency(0),
    maximum = ConfigSingleton.rootNote.getFrequency(1),
    base = 2F
)