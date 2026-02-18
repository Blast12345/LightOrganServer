package input.audioInput

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import wrappers.sound.InputLine

// ENHANCEMENT: Handle unexpected disconnects.
class AudioInput(
    private val inputLine: InputLine,
    private val sampleBuffer: SampleBuffer,
    private val sampleNormalizer: SampleNormalizer,
    private val scope: CoroutineScope
) {

    val name: String = inputLine.name
    val sampleRate: Int = inputLine.sampleRate
    val bitDepth: Int = inputLine.bitDepth
    val channels: Int = inputLine.channels

    private val _isListening: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isListening = _isListening.asStateFlow()

    private val _sampleUpdates = MutableSharedFlow<FloatArray>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sampleUpdates = _sampleUpdates.asSharedFlow()

    private var listeningJob: Job? = null

    suspend fun start() {
        inputLine.start()
        _isListening.value = true

        listeningJob = scope.launch {
            try {
                startReadLoop()
            } finally {
                _isListening.value = false
            }
        }
    }

    private suspend fun startReadLoop() {
        while (currentCoroutineContext().isActive) {
            val inputData = inputLine.read()
            val newSamples = sampleNormalizer.normalize(inputData)

            sampleBuffer.append(newSamples)

            _sampleUpdates.tryEmit(sampleBuffer.current)
        }
    }

    fun stop() {
        listeningJob?.cancel()
        inputLine.stop()
        _isListening.value = false
    }

}
