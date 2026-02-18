package input

import input.audioInput.AudioInput
import input.audioInput.AudioInputFinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class AudioInputManager(
    private val currentAudioInput: MutableStateFlow<AudioInput?> = MutableStateFlow(null),
    private val audioInputFinder: AudioInputFinder = AudioInputFinder(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val sharingPolicy: SharingStarted = SharingStarted.WhileSubscribed(5000)
) {

    // State
    val inputDetails: StateFlow<AudioInputDetails?> = currentAudioInput
        .map { it?.toInfo() }
        .stateIn(scope, sharingPolicy, null)

    // TODO: Put this somewhere else?
    private fun AudioInput.toInfo() = AudioInputDetails(
        name = name,
        format = AudioFormat(
            sampleRate = sampleRate,
            bitDepth = bitDepth,
            channels = channels
        ),
    )

    val isListening: StateFlow<Boolean> = currentAudioInput
        .flatMapLatest { it?.isListening ?: flowOf(false) }
        .stateIn(scope, sharingPolicy, false)

    val sampleUpdates: Flow<FloatArray> = currentAudioInput
        .flatMapLatest { it?.sampleUpdates ?: emptyFlow() }


    // Start-stop
    suspend fun startListening() {
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

