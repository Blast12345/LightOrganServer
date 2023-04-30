package input.lineListener

import config.PersistedConfig
import kotlinx.coroutines.*
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class LineListener(
    val subscribers: MutableSet<LineListenerSubscriber> = mutableSetOf(),
    private val dataLine: TargetDataLine,
    private val targetDataLineReader: TargetDataLineReader = TargetDataLineReader(),
    private val checkInterval: Long = PersistedConfig().millisecondsToWaitBetweenCheckingForNewAudio,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
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

    // TODO: Verify that this is the best approach
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

}