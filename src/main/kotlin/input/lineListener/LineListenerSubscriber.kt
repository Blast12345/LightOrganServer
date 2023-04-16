package input.lineListener

interface LineListenerSubscriber {
    fun received(newSamples: ByteArray)
}