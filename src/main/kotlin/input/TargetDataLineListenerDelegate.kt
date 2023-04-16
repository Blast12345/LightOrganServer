package input

interface TargetDataLineListenerDelegate {
    fun received(newSamples: ByteArray)
}