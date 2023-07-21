package scopes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val IoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)