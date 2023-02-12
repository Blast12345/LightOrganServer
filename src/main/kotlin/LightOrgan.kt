import color.ColorFactory
import color.ColorFactoryInterface
import colorBroadcaster.ColorBroadcaster
import colorBroadcaster.ColorBroadcasterDelegate
import config.Config
import sound.input.InputDelegate
import sound.input.samples.AudioSignal
import java.awt.Color

class LightOrgan : InputDelegate, ColorBroadcasterDelegate {

    private val audioCache: AudioCacheInterface
    private val colorBroadcaster: ColorBroadcaster
    private val colorFactory: ColorFactoryInterface

    constructor(
        audioCache: AudioCacheInterface,
        colorBroadcaster: ColorBroadcaster,
        colorFactory: ColorFactoryInterface
    ) {
        this.audioCache = audioCache
        this.colorBroadcaster = colorBroadcaster
        this.colorFactory = colorFactory
    }

    constructor(config: Config) {
        audioCache = AudioCache()
        colorBroadcaster = ColorBroadcaster(delegate = this, config = config)
        colorFactory = ColorFactory(config)
    }

    init {
        // TODO: What thread should delegates fire on?
        startListeningForAudio()
    }

    private fun startListeningForAudio() {
        // TODO:
//        input.listenForAudio(this)
    }

    override fun received(audioSignal: AudioSignal) {
        audioCache.setAudio(audioSignal)
    }

    override fun getNextColor(): Color? {
        val audio = audioCache.getAudio()
        audioCache.clear()

        return if (audio != null) {
            colorFactory.create(audio)
        } else {
            null
        }
    }

}

