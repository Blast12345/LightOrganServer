package colorListener.sound.input

class FakeLineInputListener : LineInputListenerInterface {

    var nextAudioSample: NextAudioSample? = null

    override fun listenForNextAudioSample(lambda: NextAudioSample) {
        nextAudioSample = lambda
    }

}