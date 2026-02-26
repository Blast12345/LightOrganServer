package lightOrgan

import audio.samples.AudioFrame
import color.ColorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import server.Server
import utilities.TimestampUtility

class LightOrgan(
    private val audioInputManager: AudioInputManager,
    private val spectrumManager: SpectrumManager,
    private val colorFactory: ColorFactory = ColorFactory(), // TODO: Refactor
    private val server: Server = Server(),
    private val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
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
        val color = colorFactory.create(frequencyBins)

        subscribers.forEach { it.new(color) }
        server.new(color)

        timeBetweenColors.logTimeSinceLast()
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}