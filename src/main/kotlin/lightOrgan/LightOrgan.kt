package lightOrgan

import audio.samples.AudioFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lightOrgan.color.ColorManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import server.Server
import utilities.SequenceGapDetector
import utilities.TimestampUtility

// ENHANCEMENT: Gracefully handle crashed coroutines
class LightOrgan(
    val inputManager: AudioInputManager,
    val spectrumManager: SpectrumManager,
    val colorManager: ColorManager,
    private val server: Server = Server()
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")

    fun start(scope: CoroutineScope) {
        val gapDetector = SequenceGapDetector("Audio stream") // TODO: Remove me?

        inputManager.audioStream
            .onEach { gapDetector.check(it.sequenceNumber) }
            .onEach { handle(it.audio) }
            .launchIn(scope)
    }

    private suspend fun handle(newAudio: AudioFrame) {
        val frequencyBins = spectrumManager.calculate(newAudio)
        val color = colorManager.calculate(frequencyBins)

        server.new(color)

        timeBetweenColors.logTimeSinceLast()
    }

}