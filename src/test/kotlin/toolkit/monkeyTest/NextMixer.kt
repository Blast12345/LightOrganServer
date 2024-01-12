package toolkit.monkeyTest

import io.mockk.mockk
import javax.sound.sampled.Mixer

fun nextMixer(): Mixer {
    return mockk()
}
