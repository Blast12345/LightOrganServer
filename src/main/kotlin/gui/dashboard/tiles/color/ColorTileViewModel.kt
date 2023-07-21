package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import extensions.toComposeColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lightOrgan.LightOrganSubscriber

class ColorTileViewModel(
    val color: MutableState<Color>,
    val scope: CoroutineScope
) : LightOrganSubscriber {

    override fun new(color: java.awt.Color) {
        scope.launch {
            this@ColorTileViewModel.color.value = color.toComposeColor()
        }
    }

}