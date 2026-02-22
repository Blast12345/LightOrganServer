package gui.basicComponents

import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SimpleButton(
    title: String,
    isLoading: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = !isLoading,
        onClick = { action.invoke() },
        modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(title)
        }
    }
}