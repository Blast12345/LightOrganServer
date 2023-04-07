package gui.tiles.Synesthetic

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.DashboardViewModel
import gui.basicComponents.LabelledCheckbox
import gui.basicComponents.Tile
import gui.wrappers.SimpleSpacer
import gui.wrappers.SimpleText
import gui.wrappers.SimpleTooltipArea

@Composable
fun SynestheticTile(
    dashboardViewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(16)
        startAutomaticallyToggle(dashboardViewModel)
        SimpleSpacer(16)
        startOrStopButton(dashboardViewModel)
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
private fun startAutomaticallyToggle(dashboardViewModel: DashboardViewModel) {
    SimpleTooltipArea(
        text = "Start automatically when the application is launched.",
        content = { startAutomaticallyLabelledCheckbox(dashboardViewModel) }
    )
}

@Composable
private fun startAutomaticallyLabelledCheckbox(dashboardViewModel: DashboardViewModel) {
    LabelledCheckbox(
        label = "Start automatically",
        isChecked = dashboardViewModel.startAutomatically.value,
        didChange = { dashboardViewModel.startAutomaticallyPressed(it) }
    )
}

@Composable
private fun startOrStopButton(dashboardViewModel: DashboardViewModel) {
    if (dashboardViewModel.isRunning.value) {
        stopButton(dashboardViewModel)
    } else {
        startButton(dashboardViewModel)
    }
}

@Composable
private fun startButton(dashboardViewModel: DashboardViewModel) {
    Button(
        content = { Text("Start") },
        onClick = { dashboardViewModel.startPressed() }
    )
}

@Composable
private fun stopButton(dashboardViewModel: DashboardViewModel) {
    Button(
        content = { Text("Stop") },
        onClick = { dashboardViewModel.stopPressed() }
    )
}
