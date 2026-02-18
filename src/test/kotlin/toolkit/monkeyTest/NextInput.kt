package toolkit.monkeyTest

import input.audioInput.AudioInput
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

data class TestAudioInput(
    val mock: AudioInput,
    val isListeningFlow: MutableStateFlow<Boolean>
)

fun nextAudioInput(
    name: String = nextString()
): TestAudioInput {
    val audioInput: AudioInput = mockk(relaxed = true)
    val isListeningFlow = MutableStateFlow(false)

    every { audioInput.isListening } returns isListeningFlow
    every { audioInput.name } returns name
    every { audioInput.sampleRate } returns nextPositiveInt()
    every { audioInput.bitDepth } returns nextPositiveInt()

    return TestAudioInput(audioInput, isListeningFlow)
}