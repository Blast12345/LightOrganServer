package lightOrgan

import audio.samples.AccumulatingAudioBuffer
import audio.samples.AudioFrame
import color.ColorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import server.Server
import utilities.SequenceGapDetector
import utilities.TimestampUtility
import java.util.concurrent.ConcurrentHashMap

// ENHANCEMENT: Gracefully handle crashed coroutines
class LightOrgan(
    private val audioInputManager: AudioInputManager,
    private val spectrumManager: SpectrumManager,
    private val colorFactory: ColorFactory = ColorFactory(), // TODO: Refactor
    private val server: Server = Server(),
    private val audioBuffer: AccumulatingAudioBuffer = AccumulatingAudioBuffer(),
    private val subscribers: MutableSet<LightOrganSubscriber> = ConcurrentHashMap.newKeySet(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")

    init {
        // Collection and calculation are separate jobs so that slow calculations don't block a collection.
        startCollectingAudio()
        startCalculatingColors()
    }

    private fun startCollectingAudio() {
        scope.launch {
            val gapDetector = SequenceGapDetector("Audio stream")

            audioInputManager.audioStream.collect { frame ->
                gapDetector.check(frame.sequenceNumber)
                audioBuffer.append(frame.audio)
            }
        }
    }

    private fun startCalculatingColors() {
        scope.launch {
            while (isActive) {
                val audio = audioBuffer.drain()
                handle(audio)
            }
        }
    }

    private fun handle(newAudio: AudioFrame) {
        val frequencyBins = spectrumManager.calculate(newAudio)
        val color = colorFactory.create(frequencyBins)

        subscribers.forEach { it.new(color) }
        server.new(color)

        timeBetweenColors.logTimeSinceLast()
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}