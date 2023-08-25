package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lightOrgan.LightOrganSubscriber

class ColorTileViewModel(
    val color: MutableState<Color>,
    val scope: CoroutineScope
) : LightOrganSubscriber {

    override fun new(color: wrappers.color.Color) {
        scope.launch {
            this@ColorTileViewModel.color.value = getComposeColor(color)
        }
    }

    private fun getComposeColor(color: wrappers.color.Color): Color {
        return Color.hsv(
            hue = color.hue * 360,
            saturation = color.saturation,
            value = color.brightness
        )
    }

}