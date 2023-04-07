package lightOrgan.sound.input

import config.Config
import config.ConfigSingleton
import kotlinx.coroutines.*
import javax.sound.sampled.TargetDataLine

class TargetDataLineListener(
    val listeners: MutableSet<TargetDataLineListenerDelegate> = mutableSetOf(),
    private val dataLine: TargetDataLine,
    private val config: Config = ConfigSingleton,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {

    init {
        prepareDataLine()
        startListeningForAudio()
    }

    private fun prepareDataLine() {
        dataLine.open()
        dataLine.start()
    }

    // NOTE: This seems very computationally expensive.
    // Is there a better way to accomplish this?
    private fun startListeningForAudio() {
        scope.launch {
            while (isActive) {
                giveDelegateNewSamplesIfAble()
                enforceRateLimit()
            }
        }
    }

    private fun giveDelegateNewSamplesIfAble() {
        val bytesToRead = dataLine.available()
        val hasBytesToRead = bytesToRead > 0

        if (hasBytesToRead) {
            giveDelegateNewSamples(bytesToRead)
        }
    }

    private fun giveDelegateNewSamples(bytesToRead: Int) {
        val newSamples = getNewSamples(bytesToRead)

        listeners.forEach {
            it.received(newSamples, dataLine.format)
        }
    }

    private fun getNewSamples(bytes: Int): ByteArray {
        val newSamples = ByteArray(bytes)
        dataLine.read(newSamples, 0, bytes)
        return newSamples
    }

    private suspend fun enforceRateLimit() {
        delay(config.millisecondsToWaitBetweenCheckingForNewAudio)
    }

}