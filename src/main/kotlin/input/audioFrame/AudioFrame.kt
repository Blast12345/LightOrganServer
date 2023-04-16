package input.audioFrame

import wrappers.audioFormat.AudioFormatWrapper

data class AudioFrame(val samples: DoubleArray, val format: AudioFormatWrapper)