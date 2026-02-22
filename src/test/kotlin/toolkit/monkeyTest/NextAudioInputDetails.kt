package toolkit.monkeyTest

import lightOrgan.input.AudioInputDetails

fun nextAudioInputDetails(): AudioInputDetails {
    return AudioInputDetails(
        name = nextString("lightOrgan/input"),
        format = nextAudioFormat(),
    )
}