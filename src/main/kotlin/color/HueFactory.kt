package color

import color.scales.HueScale
import color.scales.NoteScale

class HueFactory(
    private val noteScale: NoteScale = NoteScale(),
    private val hueScale: HueScale = HueScale()
) {

    fun create(frequency: Float): Float {
        val normalizedValue = noteScale.normalize(frequency)
        val scaledHue = hueScale.scale(normalizedValue)
        return scaledHue % 1
    }

}