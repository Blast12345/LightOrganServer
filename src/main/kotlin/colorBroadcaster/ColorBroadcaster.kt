package colorBroadcaster

import config.Config
import kotlinx.coroutines.*
import server.Server
import server.ServerInterface
import java.awt.Color

class ColorBroadcaster(
    scope: CoroutineScope = MainScope(),
    private val config: Config,
    private val delegate: ColorBroadcasterDelegate,
    private val server: ServerInterface = Server(config),
) {

    init {
        scope.launch {
            while (isActive) {
                broadcastNextColorIfAble()
                delay(config.millisecondsToWaitBetweenCheckingForNextColor)
            }
        }
    }

    private fun broadcastNextColorIfAble() {
        getNextColor()?.let {
            broadcast(it)
        }
    }

    private fun getNextColor(): Color? {
        return delegate.getNextColor()
    }

    private fun broadcast(color: Color) {
        server.sendColor(color)
    }

}