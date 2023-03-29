import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import gui.ViewModel
import gui.shared.Theme
import gui.tiles.ColorTile
import gui.tiles.Stats.StatsTile
import gui.tiles.Synesthetic.SynestheticTile

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Synesthetic",
        state = rememberWindowState(width = 900.dp, height = 600.dp),
    ) {
        gui()
    }
}

@Preview
@Composable
private fun gui() {
    Theme {
        Background()
        MainRow()
    }
}

@Composable
private fun Background() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    )
}

@Composable
private fun MainRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val viewModel = remember { ViewModel() }

        SynestheticTile(
            viewModel = viewModel.synestheticViewModel.value,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        ColorTile(
            viewModel = viewModel,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        StatsTile(
            viewModel = viewModel.statsViewModel.value,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}
