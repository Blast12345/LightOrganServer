package gui.dashboard.tiles.color

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

class ColorViewModelFactory {

    fun create(): ColorViewModel {
        return ColorViewModel(
            color = mutableStateOf(Color.Black)
        )
    }

}