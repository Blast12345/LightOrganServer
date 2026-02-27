package lightOrgan

import audio.samples.AudioFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lightOrgan.color.ColorManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import server.Server
import utilities.TimestampUtility

class LightOrgan(
    val audioInputManager: AudioInputManager,
    val spectrumManager: SpectrumManager,
    val colorManager: ColorManager,
    private val server: Server = Server(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")

    init {
        // TODO: How to handle exceptions?
        scope.launch {
            audioInputManager.bufferedAudio.collect { handle(it) }
        }
    }

    private fun handle(newAudio: AudioFrame) {
        val frequencyBins = spectrumManager.calculate(newAudio)
        val color = colorManager.calculate(frequencyBins)

        server.new(color)

        timeBetweenColors.logTimeSinceLast()
    }

}