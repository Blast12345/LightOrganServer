package lightOrgan.input

import audio.samples.AudioFrame
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

data class AudioInputManagerFixture(
    val mock: AudioInputManager,
    val inputDetailsFlow: MutableStateFlow<AudioInputDetails?>,
    val isListeningFlow: MutableStateFlow<Boolean>,
    val bufferedAudio: MutableSharedFlow<AudioFrame>
) {

    companion object {
        fun create(): AudioInputManagerFixture {
            val fixture = AudioInputManagerFixture(
                mock = mockk<AudioInputManager>(),
                inputDetailsFlow = MutableStateFlow(null),
                isListeningFlow = MutableStateFlow(false),
                bufferedAudio = MutableSharedFlow(extraBufferCapacity = 1)
            )

            every { fixture.mock.inputDetails } returns fixture.inputDetailsFlow
            every { fixture.mock.isListening } returns fixture.isListeningFlow
            every { fixture.mock.bufferedAudio } returns fixture.bufferedAudio

            return fixture
        }
    }

}