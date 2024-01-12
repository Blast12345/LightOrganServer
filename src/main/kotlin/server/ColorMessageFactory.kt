package server

import wrappers.color.Color

class ColorMessageFactory {

    fun create(color: Color): String {
        return "${color.red},${color.green},${color.blue}"
    }

}
