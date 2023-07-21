package gui.dashboard.tiles.lightOrgan

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.*

@Composable
fun LightOrganTile(
    viewModel: LightOrganTileViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()

        SimpleSpacer(16)

        val startAutomatically by viewModel.startAutomatically.collectAsState()
        startAutomaticallyToggle(
            startAutomatically = startAutomatically,
            didChange = { viewModel.startAutomatically.value = it }
        )

        SimpleSpacer(16)

        val isRunning by viewModel.isRunning.collectAsState()
        startOrStopButton(
            isRunning = isRunning,
            startAction = { viewModel.start() },
            stopAction = { viewModel.stop() }
        )
    }
}

@Composable
private fun title() {
    SimpleText(
        text = "Light Organ",
        fontSize = 32,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun startAutomaticallyToggle(
    startAutomatically: Boolean,
    didChange: (Boolean) -> Unit
) {
    SimpleTooltipArea(
        text = "Start automatically when the application is launched.",
        content = { startAutomaticallyLabelledCheckbox(startAutomatically, didChange) }
    )
}

@Composable
private fun startAutomaticallyLabelledCheckbox(
    startAutomatically: Boolean,
    didChange: (Boolean) -> Unit
) {
    LabelledCheckbox(
        label = "Start automatically",
        isChecked = startAutomatically,
        didChange = didChange
    )
}

@Composable
private fun startOrStopButton(
    isRunning: Boolean,
    startAction: () -> Unit,
    stopAction: () -> Unit
) {
    if (isRunning) {
        stopButton(stopAction)
    } else {
        startButton(startAction)
    }
}

@Composable
private fun startButton(startAction: () -> Unit) {
    Button(
        content = { Text("Start") },
        onClick = startAction
    )
}

@Composable
private fun stopButton(stopAction: () -> Unit) {
    Button(
        content = { Text("Stop") },
        onClick = stopAction
    )
}
