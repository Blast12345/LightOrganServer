package toolkit.monkeyTest

import input.audioFrame.AudioFrame
import wrappers.audioFormat.AudioFormatWrapper

fun nextAudioFrame(
    samples: DoubleArray = nextDoubleArray(),
    format: AudioFormatWrapper = nextAudioFormatWrapper()
): AudioFrame {
    return AudioFrame(
        samples = samples,
        format = format
    )
}
