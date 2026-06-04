package lightOrgan

import audio.samples.AudioFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lightOrgan.color.ColorManager
import lightOrgan.gateway.GatewayManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import utilities.SequenceGapDetector
import utilities.TimestampUtility

// ENHANCEMENT: Gracefully handle crashed coroutines
class LightOrgan(
    private val inputManager: AudioInputManager,
    private val spectrumManager: SpectrumManager,
    private val colorManager: ColorManager,
    private val gatewayManager: GatewayManager,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")

    fun start() {
        inputManager.selectDefaultInput()
        val gapDetector = SequenceGapDetector("Audio stream")

        // ENHANCEMENT: Decouple ingest and calculation
        inputManager.audioStream
            // WARNING: Overflowing the buffer will cause spectral artifacts
            .buffer(64, onBufferOverflow = BufferOverflow.DROP_OLDEST)
            .onEach { gapDetector.check(it.sequenceNumber) }
            .onEach { handle(it.audio) }
            .launchIn(scope)
    }

    private suspend fun handle(newAudio: AudioFrame) {
        val frequencyBins = spectrumManager.calculate(newAudio)
        val color = colorManager.calculate(frequencyBins)

        val state = gatewayManager.connectionState.value
        if (state is GatewayManager.State.Connected) {
            state.gateway.broadcastColor(color)
        }

        timeBetweenColors.logTimeSinceLast()
    }

}