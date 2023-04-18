package gui.dashboard

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gui.Theme
import gui.dashboard.tiles.Color.ColorTile
import gui.dashboard.tiles.Statistics.StatisticsTile
import gui.dashboard.tiles.Synesthetic.SynestheticTile

@Preview
@Composable
fun Dashboard() {
    val viewModel = remember { DashboardViewModel() }

    Theme {
        Background()
        MainRow(viewModel)
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
private fun MainRow(viewModel: DashboardViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(16.dp)
    ) {

        SynestheticTile(
            dashboardViewModel = viewModel,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        ColorTile(
            dashboardViewModel = viewModel,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        StatisticsTile(
            viewModel = viewModel.statisticsViewModelState.value,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

    }
}
