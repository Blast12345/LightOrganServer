package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import extensions.toComposeColor
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lightOrgan.LightOrganSubscriber
import kotlin.random.Random

class ColorViewModel(
    val color: MutableState<Color>
) : LightOrganSubscriber {
    
//    private val red = Random.nextFloat()
//    private val green = Random.nextFloat()
//    private val blue = Random.nextFloat()
//    private val awtColor = java.awt.Color(red, green, blue)
//    private val composeColor = androidx.compose.ui.graphics.Color(red, green, blue)

    override fun new(color: java.awt.Color) {
        MainScope().launch {
            this@ColorViewModel.color.value = color.toComposeColor()
        }
    }

}