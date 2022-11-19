package colorListener.sound.input

typealias NextAudioSample = (sampleRate: Int, sampleSize: Int, signal: DoubleArray) -> Unit

interface LineInputListenerInterface {
    fun listenForNextAudioSample(lambda: NextAudioSample)
}

class LineInputListener : LineInputListenerInterface {

    override fun listenForNextAudioSample(lambda: NextAudioSample) {
        // TODO:
    }

}