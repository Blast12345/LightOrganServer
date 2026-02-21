package audio.audioInput

import audio.audioInput.AudioInput
import audio.samples.AudioFrame
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import toolkit.monkeyTest.nextAudioFormat

data class MockAudioInput(
    val mock: AudioInput,
    val isListeningFlow: MutableStateFlow<Boolean>,
    val bufferedAudioFlow: MutableSharedFlow<AudioFrame>
) {

    companion object {
        fun create(name: String): MockAudioInput {
            val mock = mockk<AudioInput>()
            val isListeningFlow = MutableStateFlow(false)
            // NOTE: extra buffer to prevent emits from blocking thread
            val bufferedAudioFlow = MutableSharedFlow<AudioFrame>(extraBufferCapacity = 1)

            every { mock.name } returns name
            every { mock.format } returns nextAudioFormat()
            every { mock.start() } returns Unit
            every { mock.stop() } returns Unit
            every { mock.isListening } returns isListeningFlow
            every { mock.bufferedAudio } returns bufferedAudioFlow

            return MockAudioInput(mock, isListeningFlow, bufferedAudioFlow)
        }
    }
}