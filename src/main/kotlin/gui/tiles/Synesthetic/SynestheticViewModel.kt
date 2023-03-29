package gui.tiles.Synesthetic

import LightOrgan
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sound.input.Input

class SynestheticViewModel(
    val input: Input,
    val startAutomatically: MutableState<Boolean> = mutableStateOf(false),
    val isRunning: MutableState<Boolean> = mutableStateOf(false)
) {

    private var lightOrgan: LightOrgan? = null

    fun startAutomatically(value: Boolean) {
        // TODO:
    }

    fun startPressed() {
        // TODO: Loading indicator?
        GlobalScope.launch {
            lightOrgan = LightOrgan(
                newColor = { /*currentColor.value = it.toComposeColor()*/ },
                input = input
            )

            updateIsRunningState()
        }
    }

    fun stopPressed() {
        // TODO: Loading indicator?
        GlobalScope.launch {
            lightOrgan?.stop()
            lightOrgan = null
            updateIsRunningState()
        }
    }

    private fun updateIsRunningState() {
        // TODO: Maybe a simple isRunning bool on the LO?
        isRunning.value = (lightOrgan != null)
    }

}