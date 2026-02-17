package gui.basicComponents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SimpleSpacer(size: Dp) {
    Spacer(Modifier.size(size))
}
