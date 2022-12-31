package sound.input

class FakeInput : InputInterface {

    var listener: NextAudioSample? = null

    override fun listenForAudioSamples(listener: NextAudioSample) {
        this.listener = listener
    }

}