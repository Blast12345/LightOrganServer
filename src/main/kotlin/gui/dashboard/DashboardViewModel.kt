package gui.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import config.ConfigSingleton
import config.ConfigStorage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lightOrgan.LightOrgan
import lightOrgan.LightOrganListener
import lightOrgan.sound.input.Input
import lightOrgan.sound.input.finder.InputFinder
import server.Server


// TODO: This seems temporary
class DefaultInputFactory {

    fun create(): Input {
        val dataLine = InputFinder().getInput()
        return Input(dataLine)
    }

}

// TODO: Test me
class DashboardViewModel : LightOrganListener {

    private var input = DefaultInputFactory().create()
    private var lightOrgan = LightOrgan(input)
    private var server = Server(ConfigSingleton.clients)

    val startAutomatically = mutableStateOf(ConfigSingleton.startAutomatically)
    val isRunning = mutableStateOf(false)
    val color = mutableStateOf(Color.Black)
    val durationOfAudioUsed = mutableStateOf("")
    val lowestDiscernibleFrequency = mutableStateOf("")
    val frequencyResolution = mutableStateOf("")

    init {
        startLightOrganIfNeeded()
        subscribeToLightOrgan()
        updateDurationOfAudioUsed()
        updateLowestDiscernibleFrequency()
        updateFrequencyResolution()
    }

    private fun startLightOrganIfNeeded() {
        if (ConfigStorage().get()?.startAutomatically == true) {
            startPressed()
        }
    }

    private fun subscribeToLightOrgan() {
        lightOrgan.listeners.add(this)
    }

    fun startAutomaticallyPressed(value: Boolean) {
        ConfigSingleton.startAutomatically = value
        ConfigStorage().set(ConfigSingleton)
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

    private fun updateDurationOfAudioUsed() {
        durationOfAudioUsed.value = formattedDurationOfAudioUsed()
    }

    private fun formattedDurationOfAudioUsed(): String {
        return formatted(
            value = calculateSecondsOfAudioUsed(),
            unitOfMeasure = "seconds"
        )
    }

    private fun calculateSecondsOfAudioUsed(): Float {
        return SecondsOfAudioUsedCalculator().calculate(
            sampleRate = input.dataLine.format.sampleRate,
            sampleSize = ConfigSingleton.sampleSize,
            numberOfChannels = input.dataLine.format.channels
        )
    }

    private fun updateLowestDiscernibleFrequency() {
        lowestDiscernibleFrequency.value = formattedLowestDiscernibleFrequency()
    }

    private fun formattedLowestDiscernibleFrequency(): String {
        return formatted(
            value = calculateLowestDiscernibleFrequency(),
            unitOfMeasure = "Hz"
        )
    }

    private fun calculateLowestDiscernibleFrequency(): Float {
        return LowestDiscernibleFrequencyCalculator().calculate(
            secondsOfAudioUsed = calculateSecondsOfAudioUsed()
        )
    }

    private fun updateFrequencyResolution() {
        frequencyResolution.value = formattedFrequencyResolution()
    }

    private fun formattedFrequencyResolution(): String {
        return formatted(
            value = calculateFrequencyResolution(),
            unitOfMeasure = "Hz"
        )
    }

    private fun calculateFrequencyResolution(): Float {
        return FrequencyResolutionCalculator().calculate(
            sampleSize = ConfigSingleton.sampleSize,
            sampleRate = input.dataLine.format.sampleRate,
            numberOfChannels = input.dataLine.format.channels
        )
    }

    // Helper
    private fun formatted(value: Float, unitOfMeasure: String): String {
        val formattedValue = String.format("%.2f", value)
        return "$formattedValue $unitOfMeasure"
    }

    override fun new(color: java.awt.Color) {
        server.sendColor(color)

        MainScope().launch {
            this@DashboardViewModel.color.value = color.toComposeColor()
        }
    }

}


class SecondsOfAudioUsedCalculator {

    fun calculate(sampleSize: Int, sampleRate: Float, numberOfChannels: Int): Float {
        return sampleSize.toFloat() / sampleRate / numberOfChannels.toFloat()
    }

}

class LowestDiscernibleFrequencyCalculator {

    fun calculate(secondsOfAudioUsed: Float): Float {
        return 1 / secondsOfAudioUsed
    }

}

class FrequencyResolutionCalculator {

    // TODO: Verify this calculation
    fun calculate(sampleRate: Float, sampleSize: Int, numberOfChannels: Int): Float {
        return sampleRate / sampleSize.toFloat() * numberOfChannels.toFloat()
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