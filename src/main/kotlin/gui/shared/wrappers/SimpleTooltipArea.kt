package gui.shared.wrappers

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun SimpleTooltipPreview() {
    SimpleTooltipArea(
        text = "Sample tooltip",
        content = { Text("Foobar") }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleTooltipArea(
    text: String,
    content: @Composable () -> Unit
) {
    TooltipArea(
        tooltip = { tooltip(text) },
        content = content
    )
}

@Composable
private fun tooltip(text: String) {
    Surface(
        color = Color(255, 255, 210),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .widthIn(0.dp, 256.dp)
            .padding(start = 8.dp)
            .shadow(4.dp),
    ) {
        text(text)
    }
}

@Composable
private fun text(text: String) {
    SimpleText(
        text = text,
        fontSize = 12,
        padding = 8
    )
}