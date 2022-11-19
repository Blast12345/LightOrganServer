package colorListener.sound.input

class FakeLineInputListener : LineInputListenerInterface {

    var newAudioSample: AudioSample? = null

    override fun listenForNextAudioSample(lambda: AudioSample) {
        newAudioSample = lambda
    }

}