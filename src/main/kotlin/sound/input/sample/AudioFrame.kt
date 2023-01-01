package sound.input.sample

import javax.sound.sampled.AudioFormat

data class AudioFrame(val samples: ByteArray, val format: AudioFormat)