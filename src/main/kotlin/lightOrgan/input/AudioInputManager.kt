package lightOrgan.input

import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFinder
import audio.samples.AudioStreamFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import utilities.DerivedStateFlow

// ENHANCEMENT: Handle unexpected disconnects.
// ENHANCEMENT: Configure the read size. Note that different systems may behave unusually if the value is too low/high.
@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputManager(
    private val currentAudioInput: MutableStateFlow<AudioInput?> = MutableStateFlow(null),
    private val audioInputFinder: AudioInputFinder = AudioInputFinder(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob()),
    private val sharingPolicy: SharingStarted = SharingStarted.WhileSubscribed(5000)
) {

    val inputDetails: StateFlow<AudioInputDetails?> = DerivedStateFlow(currentAudioInput) {
        it?.let { AudioInputDetails(it.name, it.format) }
    }

    val isListening: StateFlow<Boolean> = DerivedStateFlow(currentAudioInput) {
        it?.isListening?.value ?: false
    }

    val audioStream: SharedFlow<AudioStreamFrame> = currentAudioInput
        .flatMapLatest { it?.audioStream ?: emptyFlow() }
        .shareIn(scope, sharingPolicy)

    // Start-stop
    fun startListening() {
        if (currentAudioInput.value == null) selectDefaultInput()

        currentAudioInput.value?.start()
    }

    fun stopListening() {
        currentAudioInput.value?.stop()
    }

    // Input selection
    fun selectDefaultInput() {
        currentAudioInput.value?.stop()
        currentAudioInput.value = audioInputFinder.findDefaultInput()
    }

}