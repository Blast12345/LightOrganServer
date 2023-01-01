package color

import sound.input.sample.AudioFrame
import java.awt.Color

class FakeColorFactory : ColorFactoryInterface {

    var audioFrame: AudioFrame? = null
    val color: Color = Color.orange

    override fun colorFor(audioFrame: AudioFrame): Color {
        this.audioFrame = audioFrame
        return color
    }

}