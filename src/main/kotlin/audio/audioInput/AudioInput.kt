package audio.audioInput

import audio.samples.AudioFormat
import audio.samples.AudioFrame
import audio.samples.SampleNormalizer
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import logging.Logger
import scopes.IoScope
import wrappers.sound.InputLine

class AudioInput(
    private val inputLine: InputLine,
    private val sampleNormalizer: SampleNormalizer,
    private val scope: CoroutineScope = IoScope
) {

    private var listeningJob: Job? = null
    private val _isListening: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // NOTE: A small buffer to handle OS level bursts, not necessarily slow collectors.
    // It is still advised to have collectors be minimally complex or the buffer could fill up anyway.
    // It's not the responsibility of this class to cater to the performance needs of collectors.
    private val _audioStream = MutableSharedFlow<AudioFrame>(0, 8, BufferOverflow.DROP_OLDEST)

    val name: String = inputLine.name
    val format = AudioFormat(
        sampleRate = inputLine.sampleRate,
        bitDepth = inputLine.bitDepth,
        channels = inputLine.channels
    )
    val isListening = _isListening.asStateFlow()
    val audioStream = _audioStream.asSharedFlow()

    fun start() {
        if (listeningJob?.isActive == true) return

        inputLine.start()
        startCapturingAudio()
    }

    private fun startCapturingAudio() {
        listeningJob = scope.launch {
            _isListening.value = true
            startReadLoop()
        }.also { it.invokeOnCompletion(::onStoppedCapturingAudio) }
    }

    private suspend fun startReadLoop() {
        while (currentCoroutineContext().isActive) {
            val inputData = inputLine.read()
            val newSamples = sampleNormalizer.normalize(inputData)

            val audioFrame = AudioFrame(newSamples, format)
            val emittedSuccessfully = _audioStream.tryEmit(audioFrame)

            if (!emittedSuccessfully) {
                Logger.warning("Failed to emit audio frame to audioStream.")
            }
        }
    }

    private fun onStoppedCapturingAudio(cause: Throwable?) {
        _isListening.value = false

        if (cause != null && cause !is CancellationException) {
            Logger.error(cause.message ?: "$name read loop has failed unexpectedly.")
        }
    }

    fun stop() {
        listeningJob?.cancel()
        inputLine.stop()
    }

}
