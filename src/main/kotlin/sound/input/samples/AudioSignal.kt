package sound.input.samples

import wrappers.audioFormat.AudioFormatWrapper

// TODO: Rename to AudioClip?
data class AudioSignal(val samples: DoubleArray, val format: AudioFormatWrapper)