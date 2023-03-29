package gui.shared.wrappers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

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
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .widthIn(0.dp, 384.dp)
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