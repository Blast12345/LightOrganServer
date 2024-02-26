package gui.basicComponents

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowWithEqualColumnWidths(
    children: List<@Composable () -> Unit>
) {
    if (children.isEmpty()) return

    BoxWithConstraints {
        val columnWidth = maxWidth / children.size

        Row {
            children.dropLast(1).forEach { child ->
                Box(modifier = Modifier.width(columnWidth)) {
                    child()
                }
            }

            // Last child fills the remaining width
            Box(modifier = Modifier.fillMaxWidth()) {
                children.last()()
            }
        }
    }
}
