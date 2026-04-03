package lightOrgan

import audio.samples.AudioFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import lightOrgan.color.ColorManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import server.Server
import utilities.SequenceGapDetector
import utilities.TimestampUtility

// ENHANCEMENT: Gracefully handle crashed coroutines
// ENHANCEMENT: Handle when cachedAudio starts to grow significantly - it's a sign that the computer is too slow for the settings.
class LightOrgan(
    val inputManager: AudioInputManager,
    val spectrumManager: SpectrumManager,
    val colorManager: ColorManager,
    private val server: Server = Server(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")
    private val cachedAudio = Channel<AudioFrame>(Channel.UNLIMITED)

    init {
        // Collection and calculation are separate jobs so that slow calculations don't block a collection.
        startCollectingAudio()
        startCalculatingColors()
    }

    private fun startCollectingAudio() {
        scope.launch {
            val gapDetector = SequenceGapDetector("Audio stream")

            inputManager.audioStream.collect { streamFrame ->
                gapDetector.check(streamFrame.sequenceNumber)
                cachedAudio.trySend(streamFrame.audio)
            }
        }
    }

    private fun startCalculatingColors() {
        scope.launch {
            while (isActive) {
                val audio = cachedAudio.receive()
                handle(audio)
            }
        }
    }

    private fun handle(newAudio: AudioFrame) {
        val frequencyBins = spectrumManager.calculate(newAudio)
        val color = colorManager.calculate(frequencyBins)

//        server.new(color)

//        timeBetweenColors.logTimeSinceLast()
    }

}