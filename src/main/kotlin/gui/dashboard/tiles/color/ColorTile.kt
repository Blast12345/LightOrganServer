package gui.dashboard.tiles.color

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.SimpleSpacer
import gui.basicComponents.SimpleText
import gui.basicComponents.Tile

// ENHANCEMENT: Fullscreen/second window option
@Preview
@Composable
fun ColorTile(
    viewModel: ColorTileViewModel,
    modifier: Modifier = Modifier
) {
    val colors = viewModel.colors.collectAsState()

    Tile(modifier) {
        Title()
        SimpleSpacer(dpSize = 12)
        ColorBox(colors)
    }
}

@Composable
private fun Title() {
    SimpleText(
        text = "Color",
        fontSize = 24,
        fontWeight = FontWeight.SemiBold
    )
}

//@Composable
//private fun ColorBox(color: State<Color>) {
//    Canvas(
//        Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//    ) {
//        drawRect(color = color.value)
//    }
//}

//@Composable
//private fun ColorBox(colors: State<List<Color>>) {
//    Canvas(
//        Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//    ) {
//        val halfWidth = size.width / 2
//        val halfHeight = size.height / 2
//
//        val barColor = Color.LightGray
//        val bgColor = Color.DarkGray
//
//        colors.value.forEachIndexed { index, color ->
//            val col = index % 2
//            val row = index / 2
//            val quadrantLeft = col * halfWidth
//            val quadrantTop = row * halfHeight
//
//            // Background
//            drawRect(
//                color = bgColor,
//                topLeft = Offset(quadrantLeft, quadrantTop),
//                size = Size(halfWidth, halfHeight)
//            )
//
//            // Bar height from brightness (HSV value = max of RGB)
//            val brightness = maxOf(color.red, color.green, color.blue)
//            val barHeight = brightness * halfHeight
//
//            drawRect(
//                color = barColor,
//                topLeft = Offset(quadrantLeft, quadrantTop + halfHeight - barHeight),
//                size = Size(halfWidth, barHeight)
//            )
//        }
//    }
//}

@Composable
private fun ColorBox(colors: State<List<Color>>) {
    Canvas(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val halfWidth = size.width / 2
        val halfHeight = size.height / 2

        // Top-left
        drawRect(
            color = colors.value[0],
            topLeft = Offset.Zero,
            size = Size(halfWidth, halfHeight)
        )
        // Top-right
        drawRect(
            color = colors.value[1],
            topLeft = Offset(halfWidth, 0f),
            size = Size(halfWidth, halfHeight)
        )
        // Bottom-left
        drawRect(
            color = colors.value[2],
            topLeft = Offset(0f, halfHeight),
            size = Size(halfWidth, halfHeight)
        )
        // Bottom-right
        drawRect(
            color = colors.value[3],
            topLeft = Offset(halfWidth, halfHeight),
            size = Size(halfWidth, halfHeight)
        )
    }
}