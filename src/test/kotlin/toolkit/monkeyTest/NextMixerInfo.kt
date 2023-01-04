package toolkit.monkeyTest

import io.mockk.mockk
import javax.sound.sampled.Mixer

fun nextMixerInfo(): Mixer.Info {
    return mockk()
}
