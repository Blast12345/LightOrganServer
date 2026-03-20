package lightOrgan.input

import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFinder
import audio.samples.AudioFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*

// ENHANCEMENT: Handle unexpected disconnects.
// ENHANCEMENT: Configure the read size. Note that different systems may behave unusually if the value is too low/high.
@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputManager(
    private val currentAudioInput: MutableStateFlow<AudioInput?> = MutableStateFlow(null),
    private val audioInputFinder: AudioInputFinder = AudioInputFinder(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val sharingPolicy: SharingStarted = SharingStarted.WhileSubscribed(5000)
) {

    // State
    val inputDetails: StateFlow<AudioInputDetails?> = currentAudioInput
        .map { it?.let { AudioInputDetails(it.name, it.format) } }
        .stateIn(scope, sharingPolicy, null)

    val isListening: StateFlow<Boolean> = currentAudioInput
        .flatMapLatest {
            it?.isListening ?: flowOf(false)
        }
        .stateIn(scope, sharingPolicy, false)

    val audioStream: SharedFlow<AudioFrame> = currentAudioInput
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