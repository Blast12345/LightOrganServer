package gui.tiles

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.shared.LabelledCheckbox
import gui.shared.Tile
import gui.shared.wrappers.SimpleSpacer
import gui.shared.wrappers.SimpleText
import gui.shared.wrappers.SimpleTooltipArea

@Preview
@Composable
private fun SynestheticTilePreview() {
    SynestheticTile(
        isRunning = true,
        startsAutomatically = true
    )
}

@Composable
fun SynestheticTile(
    isRunning: Boolean,
    startsAutomatically: Boolean,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(16)
        startAutomaticallyToggle(startsAutomatically)
        SimpleSpacer(16)
        startOrStopButton(isRunning)
    }
}

@Composable
private fun title() {
    SimpleText(
        text = "Synesthetic",
        fontSize = 32,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun startOrStopButton(isRunning: Boolean) {
    if (isRunning) {
        stopButton()
    } else {
        startButton()
    }
}

@Composable
private fun startButton() {
    Button(
        content = { Text("Start") },
        onClick = { println("Start was pressed!") }
    )
}

@Composable
private fun stopButton() {
    Button(
        content = { Text("Stop") },
        onClick = { println("Stop was pressed!") }
    )
}

@Composable
private fun startAutomaticallyToggle(startsAutomatically: Boolean) {
    SimpleTooltipArea(
        // TODO: Tooltip width seems wider than it should be.
        text = "Start automatically when the application is launched.",
        content = { startAutomaticallyLabelledCheckbox(startsAutomatically) }
    )
}

@Composable
private fun startAutomaticallyLabelledCheckbox(startsAutomatically: Boolean) {
    LabelledCheckbox(
        label = "Start automatically",
        isChecked = startsAutomatically,
        didChange = { println("Tapped") }
    )
}