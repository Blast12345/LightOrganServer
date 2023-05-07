package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import extensions.toComposeColor
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lightOrgan.LightOrganSubscriber

class ColorViewModel(
    val color: MutableState<Color>
) : LightOrganSubscriber {


    // TODO: Scope?
    override fun new(color: java.awt.Color) {
        MainScope().launch {
            this@ColorViewModel.color.value = color.toComposeColor()
        }
    }

}