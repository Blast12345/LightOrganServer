package sound.input.samples

// NOTE: We "normalize" the audio so that inputs of different formats (e.g. 8/16/32/64-bit)
// can all be processed the same way downstream.
// TODO: Verify consistent wording
// TODO: Add sample rate
data class NormalizedAudioFrame(val samples: DoubleArray, val sampleRate: Float)