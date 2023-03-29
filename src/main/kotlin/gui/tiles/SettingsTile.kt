package gui.tiles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsTile(modifier: Modifier) {
    Card(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text("Settings", fontSize = 20.sp)
            Row {
                apply()
                reload()
            }
        }
    }
}

@Composable
private fun apply() {
    Button(
        content = { Text("Apply") },
        onClick = { println("Apply was pressed!") }
    )
}

@Composable
private fun reload() {
    Button(
        content = { Text("Reload") },
        onClick = { println("Reload was pressed!") }
    )
}