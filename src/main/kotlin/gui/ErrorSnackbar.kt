package gui

import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

lateinit var SnackbarErrorHandler: UIErrorHandler

fun interface UIErrorHandler {
    suspend fun show(message: String)
}

@Composable
fun ErrorSnackbarHost() {
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarErrorHandler = remember {
        UIErrorHandler { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    SnackbarHost(snackbarHostState)
}