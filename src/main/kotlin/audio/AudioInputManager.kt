package audio

import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFinder
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

    val inputDetails: StateFlow<AudioInputDetails?> = currentAudioInput
        .map { it?.toInfo() }
        .stateIn(scope, sharingPolicy, null)

    val isListening: StateFlow<Boolean> = currentAudioInput
        .flatMapLatest { it?.isListening ?: flowOf(false) }
        .stateIn(scope, sharingPolicy, false)

    fun selectDefaultInput() {
        currentAudioInput.value?.stop()
        currentAudioInput.value = audioInputFinder.findDefaultInput()
    }

    suspend fun startListening() {
        if (currentAudioInput.value == null) selectDefaultInput()

        currentAudioInput.value?.start()
    }

    fun stopListening() {
        currentAudioInput.value?.stop()
    }

    private fun AudioInput.toInfo() = AudioInputDetails(
        name = name,
        sampleRate = sampleRate,
        bitDepth = bitDepth,
    )

}

