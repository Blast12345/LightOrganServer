package lightOrgan

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lightOrgan.color.ColorManager
import lightOrgan.gateway.Gateway
import lightOrgan.gateway.GatewayManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager
import utilities.TimestampUtility
import utilities.mapSequenced

// ENHANCEMENT: Gracefully handle crashed coroutines
class LightOrgan(
    private val inputManager: AudioInputManager,
    private val spectrumManager: SpectrumManager,
    private val colorManager: ColorManager,
    private val gatewayManager: GatewayManager,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
) {

    private val timeBetweenColors = TimestampUtility("Time between colors")

    fun start() {
        inputManager.selectDefaultInput()

        inputManager.audioStream
            .buffer(64, onBufferOverflow = BufferOverflow.DROP_OLDEST)
            .mapSequenced("Spectral analysis") { spectrumManager.calculate(it) }
            .conflate()
            .mapSequenced("Color generation") { colorManager.calculate(it) }
            .conflate()
            .mapSequenced("Gateway broadcast") { gatewayManager.gateway?.broadcastColor(it) }
            .onEach { timeBetweenColors.logTimeSinceLast() } // TODO: Expose as something at each layer?
            .launchIn(scope)
    }

    val GatewayManager.gateway: Gateway?
        get() = (this.state.value as? GatewayManager.State.Connected)?.gateway

}


