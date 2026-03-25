package lightOrgan

import audio.samples.AccumulatingAudioBuffer
import audio.samples.AudioFrame
import kotlinx.coroutines.*
import lightOrgan.color.ColorManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import server.Server
import utilities.TimestampUtility

// ENHANCEMENT: Gracefully handle crashed coroutines
class LightOrgan(
    val audioInputManager: AudioInputManager,
    val spectrumManager: SpectrumManager,
    val colorManager: ColorManager,
    private val server: Server = Server(),
    private val audioBuffer: AccumulatingAudioBuffer = AccumulatingAudioBuffer(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")

    init {
        // Calculating colors could be slow, and we don't want to block the audio stream, so we launch them in separate coroutines.
        startCollectingAudio()
        startCalculatingColors()
    }

    private fun startCollectingAudio() {
        scope.launch {
            audioInputManager.audioStream.collect {
                audioBuffer.append(it)
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
        val color = colorManager.calculate(frequencyBins)

//        server.new(color)

        timeBetweenColors.logTimeSinceLast()
    }

}