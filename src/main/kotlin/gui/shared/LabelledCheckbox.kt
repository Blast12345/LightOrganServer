package gui.shared

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import gui.shared.wrappers.SimpleSpacer

@Preview
@Composable
private fun LabelledCheckboxPreview() {
    LabelledCheckbox(
        label = "My Labeled Checkbox",
        isChecked = true,
        didChange = { println("Tapped") }
    )
}

@Composable
fun LabelledCheckbox(
    label: String,
    isChecked: Boolean,
    didChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                indication = rememberRipple(color = MaterialTheme.colors.primary),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { didChange(!isChecked) }
            )) {
        checkbox(isChecked)
        SimpleSpacer(8)
        text(label)
    }
}

@Composable
private fun checkbox(isChecked: Boolean) {
    Checkbox(
        checked = isChecked,
        onCheckedChange = null
    )
}

@Composable
private fun text(label: String) {
    Text(
        text = label,
        fontSize = 16.sp
    )
}