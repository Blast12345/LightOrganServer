package color

import sound.input.samples.NormalizedAudioFrame
import java.awt.Color

class FakeColorFactory : ColorFactoryInterface {

    var audioFrame: NormalizedAudioFrame? = null
    val color: Color = Color.orange

    override fun createFor(normalizedAudioFrame: NormalizedAudioFrame): Color {
        this.audioFrame = normalizedAudioFrame
        return color
    }

}