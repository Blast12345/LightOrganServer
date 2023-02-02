import sound.input.samples.AudioSignal

interface AudioCacheInterface {
    fun setAudio(audioSignal: AudioSignal)
    fun getAudio(): AudioSignal?
    fun clear()
}

// TODO: Test me
class AudioCache : AudioCacheInterface {

    private var audio: AudioSignal? = null

    override fun setAudio(audio: AudioSignal) {
        this.audio = audio
    }

    override fun getAudio(): AudioSignal? {
        return audio
    }

    override fun clear() {
        audio = null
    }

}