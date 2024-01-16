package gui.dashboard.tiles.color

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.MainScope

class ColorTileViewModelFactory {

    fun create(): ColorTileViewModel {
        return ColorTileViewModel(
            color = mutableStateOf(Color.Black),
            scope = MainScope()
        )
    }

}
