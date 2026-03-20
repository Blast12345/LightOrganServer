package server

import androidx.compose.ui.graphics.Color


class ColorMessageFactory {

    fun create(color: Color): String {
        // TODO: Scale
        return "${color.red},${color.green},${color.blue}"
    }

}
