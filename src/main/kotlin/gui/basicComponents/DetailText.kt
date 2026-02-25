package gui.basicComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun DetailText(
    label: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = "$label: ",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Text(
            text = value ?: "",
            fontSize = 12.sp
        )
    }
}