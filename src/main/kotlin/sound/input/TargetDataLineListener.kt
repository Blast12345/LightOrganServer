package sound.input

import config.Config
import config.ConfigSingleton
import kotlinx.coroutines.*
import javax.sound.sampled.TargetDataLine

class TargetDataLineListener(
    private val dataLine: TargetDataLine,
    private val config: Config = ConfigSingleton,
    private var delegate: TargetDataLineListenerDelegate? = null,
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
        delegate?.received(newSamples, dataLine.format)
    }

    private fun getNewSamples(bytes: Int): ByteArray {
        val newSamples = ByteArray(bytes)
        dataLine.read(newSamples, 0, bytes)
        return newSamples
    }

    private suspend fun enforceRateLimit() {
        delay(config.millisecondsToWaitBetweenCheckingForNewAudio)
    }

    fun setDelegate(delegate: TargetDataLineListenerDelegate) {
        this.delegate = delegate
    }

    fun getDelegate(): TargetDataLineListenerDelegate? {
        return delegate
    }

}