package color

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import wrappers.color.Color

class ColorProcessor {

    private val _lastColor = MutableStateFlow(Color.Black)
    val lastColor = _lastColor.asStateFlow()

}