package gui.tiles.Synesthetic

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

@Composable
fun SynestheticTile(
    viewModel: SynestheticViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(16)
        startAutomaticallyToggle(viewModel)
        SimpleSpacer(16)
        startOrStopButton(viewModel)
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
private fun startAutomaticallyToggle(viewModel: SynestheticViewModel) {
    SimpleTooltipArea(
        text = "Start automatically when the application is launched.",
        content = { startAutomaticallyLabelledCheckbox(viewModel) }
    )
}

@Composable
private fun startAutomaticallyLabelledCheckbox(viewModel: SynestheticViewModel) {
    LabelledCheckbox(
        label = "Start automatically",
        isChecked = viewModel.startAutomatically.value,
        didChange = { viewModel.startAutomatically(it) }
    )
}

@Composable
private fun startOrStopButton(viewModel: SynestheticViewModel) {
    if (viewModel.isRunning.value) {
        stopButton(viewModel)
    } else {
        startButton(viewModel)
    }
}

@Composable
private fun startButton(viewModel: SynestheticViewModel) {
    Button(
        content = { Text("Start") },
        onClick = { viewModel.startPressed() }
    )
}

@Composable
private fun stopButton(viewModel: SynestheticViewModel) {
    Button(
        content = { Text("Stop") },
        onClick = { viewModel.stopPressed() }
    )
}
