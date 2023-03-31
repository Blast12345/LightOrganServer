package gui.tiles.Synesthetic

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.ViewModel
import gui.basicComponents.LabelledCheckbox
import gui.basicComponents.Tile
import gui.wrappers.SimpleSpacer
import gui.wrappers.SimpleText
import gui.wrappers.SimpleTooltipArea

@Composable
fun SynestheticTile(
    viewModel: ViewModel,
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
private fun startAutomaticallyToggle(viewModel: ViewModel) {
    SimpleTooltipArea(
        text = "Start automatically when the application is launched.",
        content = { startAutomaticallyLabelledCheckbox(viewModel) }
    )
}

@Composable
private fun startAutomaticallyLabelledCheckbox(viewModel: ViewModel) {
    LabelledCheckbox(
        label = "Start automatically",
        isChecked = viewModel.startAutomatically.value,
        didChange = { viewModel.startAutomaticallyPressed(it) }
    )
}

@Composable
private fun startOrStopButton(viewModel: ViewModel) {
    if (viewModel.isRunning.value) {
        stopButton(viewModel)
    } else {
        startButton(viewModel)
    }
}

@Composable
private fun startButton(viewModel: ViewModel) {
    Button(
        content = { Text("Start") },
        onClick = { viewModel.startPressed() }
    )
}

@Composable
private fun stopButton(viewModel: ViewModel) {
    Button(
        content = { Text("Stop") },
        onClick = { viewModel.stopPressed() }
    )
}
