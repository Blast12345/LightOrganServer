package gui.tiles.color

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import color.ColorProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ColorTileViewModel(
    private val colorProcessor: ColorProcessor,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    var color by mutableStateOf(Color.Black)
        private set

    init {
        observeColor()
    }

    private fun observeColor() {
        colorProcessor.lastColor.onEach { wrappedColor ->
            color = Color.hsv(
                hue = wrappedColor.hue * 360,
                saturation = wrappedColor.saturation,
                value = wrappedColor.brightness
            )
        }.launchIn(scope)
    }

}
