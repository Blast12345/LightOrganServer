package sound.input

import config.Config
import kotlinx.coroutines.*
import javax.sound.sampled.TargetDataLine

// TODO: Test me
class TargetDataLineListener(
    private val dataLine: TargetDataLine,
    private val delegate: TargetDataLineListenerDelegate,
    private val config: Config,
    private val scope: CoroutineScope = MainScope()
) {

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
                giveDelegateNewSamplesIfAble()
                enforceCheckLimit()
            }
        }
    }

    private fun giveDelegateNewSamplesIfAble() {
        val bytesToRead = dataLine.available()
        val hasBytesToRead = bytesToRead > 0

        if (hasBytesToRead) {
            val newSamples = getNewSamples(bytesToRead)
            delegate.received(newSamples, dataLine.format) // TODO: Update test for format
        }
    }

    private fun getNewSamples(bytes: Int): ByteArray {
        val newSamples = ByteArray(bytes)
        dataLine.read(newSamples, 0, bytes)
        return newSamples
    }

    private suspend fun enforceCheckLimit() {
        delay(config.millisecondsToWaitBetweenCheckingForNewAudio)
    }

}