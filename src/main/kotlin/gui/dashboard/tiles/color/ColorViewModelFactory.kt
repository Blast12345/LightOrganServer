package gui.dashboard.tiles.color

import extensions.toComposeColor
import java.awt.Color

class ColorViewModelFactory {

    fun create(color: Color): ColorViewModel {
        return ColorViewModel(
            color = color.toComposeColor()
        )
    }

}