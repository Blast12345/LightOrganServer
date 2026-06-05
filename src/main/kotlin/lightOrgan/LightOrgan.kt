package lightOrgan

import dsp.bins.FrequencyBins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lightOrgan.color.ColorManager
import lightOrgan.gateway.Gateway
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
            .onEach { spectrumManager.calculate(it.audio) }
            .launchIn(scope)

        // TODO: Monitor for dropped frames
        spectrumManager.frequencyBins
            .onEach { handle(it) }
            .launchIn(scope)
    }

    private suspend fun handle(frequencyBins: FrequencyBins) {
        val color = colorManager.calculate(frequencyBins)

        try {
            gatewayManager.gateway?.broadcastColor(color)
        } catch (e: Exception) {
        }

        timeBetweenColors.logTimeSinceLast()
    }

    val GatewayManager.gateway: Gateway?
        get() = (this.state.value as? GatewayManager.State.Connected)?.gateway

}