package colorListener.sound.input

typealias AudioSample = (sampleRate: Int, sampleSize: Int, signal: DoubleArray) -> Unit

interface LineInputListenerInterface {
    fun listenForNextAudioSample(lambda: AudioSample)
}

class LineInputListener : LineInputListenerInterface {

    override fun listenForNextAudioSample(lambda: AudioSample) {
        // TODO:
    }

}