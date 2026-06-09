package audio.audioInput

import audio.samples.SequencedAudioFrame
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.input.AudioInputDetails
import toolkit.monkeyTest.nextAudioInputDetails

data class AudioInputFixture(
    val mock: AudioInput,
    val details: AudioInputDetails,
    val isListeningFlow: MutableStateFlow<Boolean>,
    val audioStreamFlow: MutableSharedFlow<SequencedAudioFrame>
) {

    companion object {
        fun create(name: String): AudioInputFixture {
            val mock = mockk<AudioInput>()
            val details = nextAudioInputDetails(name)
            val isListeningFlow = MutableStateFlow(false)
            val audioStreamFlow = MutableSharedFlow<SequencedAudioFrame>(extraBufferCapacity = 1)

            every { mock.name } returns details.name
            every { mock.format } returns details.format
            every { mock.start() } returns Unit
            every { mock.stop() } returns Unit
            every { mock.isListening } returns isListeningFlow
            every { mock.audioStream } returns audioStreamFlow

            return AudioInputFixture(mock, details, isListeningFlow, audioStreamFlow)
        }
    }
}