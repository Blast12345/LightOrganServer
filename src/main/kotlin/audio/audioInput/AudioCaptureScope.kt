package audio.audioInput

import kotlinx.coroutines.*

// NOTE: It's important that capture is uninterrupted, so we create a dedicated thread to minimize competing with other coroutines.
@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
val AudioCaptureScope = CoroutineScope(SupervisorJob() + newSingleThreadContext("AudioCapture"))