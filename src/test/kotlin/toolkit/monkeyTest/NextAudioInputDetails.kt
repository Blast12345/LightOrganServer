package toolkit.monkeyTest

import lightOrgan.input.AudioInputDetails

fun nextAudioInputDetails(name: String = nextString("input details")): AudioInputDetails {
    return AudioInputDetails(
        name = name,
        format = nextAudioFormat(),
    )
}