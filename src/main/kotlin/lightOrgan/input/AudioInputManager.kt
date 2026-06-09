package lightOrgan.input

import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFinder
import audio.samples.AudioFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import utilities.coroutines.Sequenced

// ENHANCEMENT: Handle unexpected disconnects.
@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputManager(
    private val currentAudioInput: MutableStateFlow<AudioInput?> = MutableStateFlow(null),
    private val audioInputFinder: AudioInputFinder = AudioInputFinder(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
) {

    val inputDetails: StateFlow<AudioInputDetails?> = currentAudioInput
        .map { it?.let { AudioInputDetails(it.name, it.format) } }
        .stateIn(scope, SharingStarted.Eagerly, null)

    val isListening: StateFlow<Boolean> = currentAudioInput
        .flatMapLatest { it?.isListening ?: flowOf(false) }
        .stateIn(scope, SharingStarted.Eagerly, false)

    val audioStream: SharedFlow<Sequenced<AudioFrame>> = currentAudioInput
        .flatMapLatest { it?.audioStream ?: emptyFlow() }
        .shareIn(scope, SharingStarted.Eagerly)

    // Input selection
    fun selectDefaultInput() {
        currentAudioInput.value?.stop()
        currentAudioInput.value = audioInputFinder.findDefaultInput()
    }

    // Start-stop
    fun startListening() {
        val input = currentAudioInput.value ?: throw IllegalStateException("Cannot start listening. No input selected.")
        input.start()
    }

    fun stopListening() {
        val input = currentAudioInput.value ?: throw IllegalStateException("Cannot stop listening. No input selected.")
        input.stop()
    }

}