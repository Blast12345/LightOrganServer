package lightOrgan.color

import dsp.fft.FrequencyBins
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import wrappers.color.Color

class ColorManager(
    private val colorCalculator: ColorCalculator = ColorCalculator(),
) {

    private val _color = MutableStateFlow(Color.black)
    val color: StateFlow<Color> = _color.asStateFlow()

    fun calculate(frequencyBins: FrequencyBins): Color {
        val color = colorCalculator.calculate(frequencyBins) ?: Color.black

        _color.value = color

        return color
    }

}