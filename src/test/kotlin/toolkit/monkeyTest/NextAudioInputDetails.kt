package toolkit.monkeyTest

import audio.AudioInputDetails

fun nextAudioInputDetails(): AudioInputDetails {
    return AudioInputDetails(
        name = nextString("input"),
        sampleRate = nextPositiveInt(),
        bitDepth = nextPositiveInt(),
    )
}