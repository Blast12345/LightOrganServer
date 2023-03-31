package gui

import LightOrgan
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import sound.input.Input
import sound.input.finder.InputFinder


// TODO: This seems temporary
class DefaultInputFactory {

    fun create(): Input {
        val dataLine = InputFinder().getInput()
        return Input(dataLine)
    }

}

// TODO: Test me
class ViewModel {

    val startAutomatically = mutableStateOf(false)
    val isRunning = mutableStateOf(false)
    val color = mutableStateOf(Color.Black)
    val durationOfAudioUsed = mutableStateOf("")
    val lowestDiscernibleFrequency = mutableStateOf("")
    val frequencyResolution = mutableStateOf("")
    val serverLatency = mutableStateOf("")
    private var input = DefaultInputFactory().create()
    private var lightOrgan = LightOrgan(input)

    fun startAutomaticallyPressed(value: Boolean) {
        // TODO:
        startAutomatically.value = value
    }

    fun startPressed() {
        lightOrgan.start()
        isRunning.value = true
    }

    fun stopPressed() {
        lightOrgan.stop()
        isRunning.value = false
    }

}


fun java.awt.Color.toComposeColor(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color.hsv(
        hue = getHue() * 360,
        saturation = getSaturation(),
        value = getBrightness()
    )
}

fun java.awt.Color.getHue(): Float {
    return java.awt.Color.RGBtoHSB(red, green, blue, null)[0]
}

fun java.awt.Color.getSaturation(): Float {
    return java.awt.Color.RGBtoHSB(red, green, blue, null)[1]
}

fun java.awt.Color.getBrightness(): Float {
    return java.awt.Color.RGBtoHSB(red, green, blue, null)[2]
}