package lightOrgan

import audio.samples.AudioFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.launchIn
import lightOrgan.color.ColorManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import logging.Logger
import server.Server
import utilities.TimestampUtility
import utilities.coroutines.mapSequenced

// ENHANCEMENT: Gracefully handle crashed coroutines
class LightOrgan(
    private val inputManager: AudioInputManager,
    private val spectrumManager: SpectrumManager,
    private val colorManager: ColorManager,
    private val server: Server = Server(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")

    fun start() {
        inputManager.selectDefaultInput()

        // ENHANCEMENT: Decouple ingest and calculation
        inputManager.audioStream
            // WARNING: Overflowing the buffer will cause spectral artifacts
            .buffer(64, onBufferOverflow = BufferOverflow.DROP_OLDEST)
            .mapSequenced(transform = { handle(it) }, onGap = logGap("Light organ pipeline"))
            .launchIn(scope)
    }

    private fun handle(newAudio: AudioFrame) {
        val frequencyBins = spectrumManager.calculate(newAudio)
        val color = colorManager.calculate(frequencyBins)

        server.new(color)

        timeBetweenColors.logTimeSinceLast()
    }


    private fun logGap(stage: String): (Long) -> Unit = { Logger.warning("$stage is slow, dropped $it") }

}