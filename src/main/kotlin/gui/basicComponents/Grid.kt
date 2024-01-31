package gui.basicComponents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

// TODO: X and Y axis labels
// TODO: Scale subdivisions based on size
@Composable
fun Grid(
    lineColor: Color = MaterialTheme.colors.primary,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1000.dp)
            .border(1.dp, lineColor)
    ) {
        content()
        Canvas(modifier = Modifier.fillMaxSize().zIndex(1f)) {
            drawGrid(this, lineColor)
        }
    }
}

fun drawGrid(scope: DrawScope, lineColor: Color) {
    val strokeWidth = 1f
    val horizontalLineInterval = scope.size.height / 10
    val verticalLineInterval = scope.size.width / 10
    for (i in 1 until 10) {
        val y = i * horizontalLineInterval
        scope.drawLine(lineColor, start = Offset(0f, y), end = Offset(scope.size.width, y), strokeWidth = strokeWidth)
    }
    for (i in 1 until 10) {
        val x = i * verticalLineInterval
        scope.drawLine(lineColor, start = Offset(x, 0f), end = Offset(x, scope.size.height), strokeWidth = strokeWidth)
    }
}
