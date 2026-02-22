package lightOrgan.input

import audio.audioInput.AudioInput
import audio.samples.AudioFrame
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class MockAudioInputManager(
    val mock: AudioInputManager,
    val currentAudioInputFlow: MutableStateFlow<AudioInput?>,
    val inputDetailsFlow: MutableStateFlow<AudioInputDetails?>,
    val isListeningFlow: MutableStateFlow<Boolean>,
    val bufferedAudio: MutableSharedFlow<AudioFrame>
) {

    companion object {
        fun create(): MockAudioInputManager {
            return MockAudioInputManager(
                mock = mockk<AudioInputManager>(),
                currentAudioInputFlow = MutableStateFlow(null),
                inputDetailsFlow = MutableStateFlow(null),
                isListeningFlow = MutableStateFlow(false),
                bufferedAudio = MutableSharedFlow(extraBufferCapacity = 1)
            )
        }
    }
    
}