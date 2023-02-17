import color.ColorFactory
import color.ColorFactoryInterface
import colorBroadcaster.ColorBroadcaster
import colorBroadcaster.ColorBroadcasterDelegate
import config.Config
import sound.input.Input
import sound.input.InputDelegate
import sound.input.samples.AudioSignal
import java.awt.Color
import javax.sound.sampled.TargetDataLine

class LightOrgan : InputDelegate, ColorBroadcasterDelegate {

    private val input: Input
    private val audioCache: AudioCacheInterface
    private val colorBroadcaster: ColorBroadcaster
    private val colorFactory: ColorFactoryInterface

    constructor(
        input: Input,
        audioCache: AudioCacheInterface,
        colorBroadcaster: ColorBroadcaster,
        colorFactory: ColorFactoryInterface
    ) {
        this.input = input
        this.audioCache = audioCache
        this.colorBroadcaster = colorBroadcaster
        this.colorFactory = colorFactory
    }

    constructor(
        dataLine: TargetDataLine,
        config: Config
    ) {
        input = Input(dataLine, config, this)
        audioCache = AudioCache()
        colorBroadcaster = ColorBroadcaster(config, this)
        colorFactory = ColorFactory(config)
    }

    override fun received(audio: AudioSignal) {
        audioCache.setAudio(audio)
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

