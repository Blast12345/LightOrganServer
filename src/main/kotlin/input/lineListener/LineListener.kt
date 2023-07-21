package input.lineListener

import config.Config
import config.ConfigSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import scopes.IoScope
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class LineListener(
    private val dataLine: TargetDataLine,
    private val scope: CoroutineScope = IoScope,
    private val targetDataLineReader: TargetDataLineReader = TargetDataLineReader(),
    private val subscribers: MutableSet<LineListenerSubscriber> = mutableSetOf(),
    private val config: Config = ConfigSingleton
) {

    private val checkInterval: Long
        get() = config.millisecondsToWaitBetweenCheckingForNewAudio

    val audioFormat: AudioFormat
        get() = dataLine.format

    init {
        prepareDataLine()
        startListeningForAudio()
    }

    private fun prepareDataLine() {
        dataLine.open()
        dataLine.start()
    }

    private fun startListeningForAudio() {
        scope.launch {
            while (isActive) {
                giveSubscribersNewSamplesIfNeeded()
                enforceRateLimit()
            }
        }
    }

    private fun giveSubscribersNewSamplesIfNeeded() {
        val newSamples = getNewSamples()

        if (newSamples.isNotEmpty()) {
            giveSubscribersNewSamples(newSamples)
        }
    }

    private fun getNewSamples(): ByteArray {
        return targetDataLineReader.getAvailableData(dataLine)
    }

    private fun giveSubscribersNewSamples(newSamples: ByteArray) {
        subscribers.forEach {
            it.received(newSamples)
        }
    }

    private suspend fun enforceRateLimit() {
        delay(checkInterval)
    }

    fun checkIfSubscribed(subscriber: LineListenerSubscriber): Boolean {
        return subscribers.contains(subscriber)
    }

    fun addSubscriber(subscriber: LineListenerSubscriber) {
        subscribers.add(subscriber)
    }

}