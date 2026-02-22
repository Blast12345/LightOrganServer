package toolkit.monkeyTest

import input.AudioInputDetails

fun nextAudioInputDetails(): AudioInputDetails {
    return AudioInputDetails(
        name = nextString("input"),
        format = nextAudioFormat(),
    )
}