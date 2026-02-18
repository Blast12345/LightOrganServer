package lightOrgan

import color.ColorFactory
import input.AudioInputManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import server.Server
import sound.FrequencyBinsCalculator
import sound.bins.frequency.BassBinsFilter
import wrappers.color.Color

class LightOrgan(
    val audioInputManager: AudioInputManager = AudioInputManager(),
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator(),
    private val frequencyBinFilter: BassBinsFilter = BassBinsFilter(),
    private val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
    private val colorFactory: ColorFactory = ColorFactory(),
    private val server: Server = Server(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    var lastColorTimestamp = System.currentTimeMillis()

    init {
        scope.launch {
            audioInputManager.sampleUpdates.collect { samples ->
                val calculationStartTimestamp = System.currentTimeMillis()
                val format = audioInputManager.inputDetails.value?.format ?: return@collect
                val bins = frequencyBinsCalculator.calculate(samples, format)
//                _frequencyBins.value = bins
                val filteredBins = frequencyBinFilter.filter(bins)
                val color = colorFactory.create(filteredBins)
//                _color.value = color
                broadcast(color)

                val calculationTime = System.currentTimeMillis() - calculationStartTimestamp
                println("Calculation time: $calculationTime")

                val timeBetweenColors = System.currentTimeMillis() - lastColorTimestamp
                println("Time between colors: $timeBetweenColors")
                lastColorTimestamp = System.currentTimeMillis()
            }
        }
    }

    private fun broadcast(color: Color) {
        subscribers.forEach { it.new(color) }
        server.new(color)
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}
