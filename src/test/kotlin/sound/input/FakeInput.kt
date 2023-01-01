package sound.input

class FakeInput : InputInterface {

    var listener: NextAudioFrame? = null

    override fun listenForAudioSamples(listener: NextAudioFrame) {
        this.listener = listener
    }

}