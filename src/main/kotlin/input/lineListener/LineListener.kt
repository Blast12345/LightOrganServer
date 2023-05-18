package input.lineListener

import config.ConfigSingleton
import kotlinx.coroutines.*
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class LineListener(
    private val dataLine: TargetDataLine,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
    private val targetDataLineReader: TargetDataLineReader = TargetDataLineReader(),
    private val subscribers: MutableSet<LineListenerSubscriber> = mutableSetOf(),
    private val checkInterval: Long = ConfigSingleton.millisecondsToWaitBetweenCheckingForNewAudio
) {

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