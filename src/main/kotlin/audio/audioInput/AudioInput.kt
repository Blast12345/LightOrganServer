package audio.audioInput

import audio.samples.AudioFormat
import audio.samples.AudioFrame
import audio.samples.AudioStreamFrame
import audio.samples.SampleNormalizer
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import logging.Logger
import scopes.IoScope
import wrappers.sound.InputLine

class DerivedStateFlow<T, R>(
    private val source: StateFlow<T>,
    private val transform: (T) -> R
) : StateFlow<R> {

    override val value: R get() = transform(source.value)

    override val replayCache: List<R> get() = listOf(value)

    override suspend fun collect(collector: FlowCollector<R>): Nothing {
        source.collect { collector.emit(transform(it)) }
    }

}

class AudioInput(
    private val inputLine: InputLine,
    private val sampleNormalizer: SampleNormalizer,
    private val scope: CoroutineScope = IoScope
) {

    private val listeningJob = MutableStateFlow<Job?>(null)


    // NOTE: The OS may not deliver samples at a consistent rate.
    // E.g., immediately after a read, more data is available, thus causing another immediate read.
    // Back-to-back reads can cause even very fast collectors to fall behind, so we allow a small buffer to handle bursts.
    // That said, collectors to be performant enough to handle high rates. If they are not, they will fall behind and experience dropped frames.
    private val _audioStream = MutableSharedFlow<AudioStreamFrame>(0, 8, BufferOverflow.DROP_OLDEST)

    val name: String = inputLine.name
    val format = AudioFormat(
        sampleRate = inputLine.sampleRate,
        bitDepth = inputLine.bitDepth,
        channels = inputLine.channels
    )

    val isListening: StateFlow<Boolean> = DerivedStateFlow(listeningJob) { it?.isActive ?: false }
    val audioStream = _audioStream.asSharedFlow()

    fun start() {
        if (isListening.value) return

        inputLine.start()
        startCapturingAudio()
    }

    private fun startCapturingAudio() {
        listeningJob.value = scope.launch {
            startReadLoop()
        }.also {
            it.invokeOnCompletion { cause ->
                if (cause != null && cause !is CancellationException) {
                    Logger.error(cause.message ?: "$name read loop has failed unexpectedly.")
                }
            }
        }
    }

    private suspend fun startReadLoop() {
        var nextSequenceNumber: Long = 0

        while (currentCoroutineContext().isActive) {
            val readResult = inputLine.read()
            val newSamples = sampleNormalizer.normalize(readResult.data)
            val audioFrame = AudioFrame(newSamples, format)
            val audioStreamFrame = AudioStreamFrame(audioFrame, nextSequenceNumber++, readResult.bufferWasFull)

            val emittedSuccessfully = _audioStream.tryEmit(audioStreamFrame)

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
        inputLine.stop()
    }

}
