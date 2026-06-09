package audio.audioInput

import audio.samples.AudioFormat
import audio.samples.AudioFrame
import audio.samples.SampleNormalizer
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import logging.Logger
import utilities.Sequenced
import wrappers.sound.InputLine

class AudioInput(
    private val inputLine: InputLine,
    private val sampleNormalizer: SampleNormalizer,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    val name: String = inputLine.name
    val format = AudioFormat(
        sampleRate = inputLine.sampleRate,
        bitDepth = inputLine.bitDepth,
        channels = inputLine.channels
    )

    // NOTE: The OS may not deliver samples at a consistent rate.
    // E.g., immediately after a read, more data is available, thus causing another immediate read.
    // Back-to-back reads can cause even very fast collectors to fall behind, so we allow a small buffer to handle bursts.
    // That said, collectors to be performant enough to handle high rates. If they are not, they will fall behind and experience dropped frames.
    private val _audioStream = MutableSharedFlow<Sequenced<AudioFrame>>(0, 8, BufferOverflow.DROP_OLDEST)
    val audioStream: SharedFlow<Sequenced<AudioFrame>> = _audioStream.asSharedFlow()

    private val listeningJob = MutableStateFlow<Job?>(null)
    val isListening: StateFlow<Boolean> = listeningJob
        .map { it != null }
        .stateIn(scope, SharingStarted.Eagerly, false)

    fun start() {
        if (listeningJob.value != null) return

        inputLine.start()

        listeningJob.value = scope.launch {
            try {
                startReadLoop()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Logger.error(e.message ?: "$name read loop has failed unexpectedly.")
            }
        }.also {
            it.invokeOnCompletion {
                inputLine.stop()
                listeningJob.value = null
            }
        }
    }

    private suspend fun startReadLoop() {
        var nextSequenceNumber: Long = 0

        while (currentCoroutineContext().isActive) {
            val readResult = inputLine.read()
            val newSamples = sampleNormalizer.normalize(readResult.data)

            val audioFrame = AudioFrame(newSamples, format)
            val sequencedStreamFrame = Sequenced("$name audio stream frame", nextSequenceNumber++, audioFrame)

            val emittedSuccessfully = _audioStream.tryEmit(sequencedStreamFrame)

            if (!emittedSuccessfully) {
                Logger.warning("Failed to emit the new audio to the stream.")
            }

            if (readResult.bufferWasFull) {
                Logger.warning("The input line's buffer was full. Samples may have been dropped.")
            }
        }
    }

    fun stop() {
        listeningJob.value?.cancel()
    }

}
