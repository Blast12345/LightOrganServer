package lightOrgan

import audio.samples.AudioFrame
import color.ColorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lightOrgan.input.AudioInputManager
import server.Server
import sound.FrequencyBinsCalculator
import sound.bins.frequency.BassBinsFilter
import utilities.TimestampUtility
import wrappers.color.Color

class LightOrgan(
    private val audioInputManager: AudioInputManager,
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator(),
    private val frequencyBinsFilter: BassBinsFilter = BassBinsFilter(), // TODO: Refactor
    private val colorFactory: ColorFactory = ColorFactory(), // TODO: Test?
    private val server: Server = Server(),
    private val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")

    init {
        scope.launch {
            audioInputManager.bufferedAudio.collect { handle(it) }
        }
    }

    private fun handle(newAudio: AudioFrame) {
        val allBins = frequencyBinsCalculator.calculate(newAudio)
        val filteredBins = frequencyBinsFilter.filter(allBins)
        val color = colorFactory.create(filteredBins)

        broadcast(color)
    }

    private fun broadcast(color: Color) {
        subscribers.forEach { it.new(color) }
        server.new(color)
        timeBetweenColors.logTimeSinceLast()
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}