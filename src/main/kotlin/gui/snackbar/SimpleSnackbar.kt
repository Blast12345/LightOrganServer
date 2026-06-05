package gui.snackbar

import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

class SimpleSnackbar {
    val controller = FlowSnackbarController()

    @Composable
    fun Host() {
        val hostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            controller.messages.collect { message ->
                hostState.showSnackbar(message)
            }
        }
        
        SnackbarHost(hostState) { data ->
            ErrorSnackbar(data)
        }
    }
}