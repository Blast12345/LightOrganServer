import color.ColorFactory
import color.ColorFactoryInterface
import config.Config
import sound.input.InputDelegate
import sound.input.InputInterface
import sound.input.samples.AudioSignal
import java.awt.Color

class LightOrgan(
    config: Config,
    private val input: InputInterface,
    private val audioCache: AudioCacheInterface = AudioCache(),
    private val colorBroadcaster: ColorBroadcasterInterface = ColorBroadcaster(),
    private val colorFactory: ColorFactoryInterface = ColorFactory(config)
) : InputDelegate, ColorBroadcasterDelegate {

    init {
        // TODO: What thread should delegates fire on?
        startListeningForAudio()
        startBroadcastingColors()
    }

    private fun startListeningForAudio() {
        input.listenForAudio(this)
    }

    private fun startBroadcastingColors() {
        colorBroadcaster.startBroadcasting(this)
    }

    override fun receivedAudio(audioSignal: AudioSignal) {
        audioCache.setAudio(audioSignal)
    }

    override fun isReadyForNextBroadcast(): Boolean {
        return audioCache.getAudio() != null
    }

    override fun getNextColor(): Color {
        val audio = audioCache.getAudio()
        audioCache.clear()

        return if (audio != null) {
            colorFactory.create(audio)
        } else {
            Color.black
        }
    }

}

