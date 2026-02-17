package lightOrgan

import audio.AudioInputManager
import color.ColorProcessor
import gateway.GatewayManager
import kotlinx.coroutines.flow.MutableStateFlow
import sound.SoundProcessor
import wrappers.color.Color

class LightOrgan(
    val audioInputManager: AudioInputManager = AudioInputManager(),
    val soundProcessor: SoundProcessor = SoundProcessor(),
    val colorProcessor: ColorProcessor = ColorProcessor(),
    val gatewayManager: GatewayManager = GatewayManager()
) {

    val isRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val lastColor: MutableStateFlow<Color> = MutableStateFlow(Color.Black)

    private var timestampOfLastSentColor = System.currentTimeMillis()

    fun start() {
//        currentInput.value?.addSubscriber(this)
//        isRunning.value = true
    }

    fun stop() {
//        currentInput.value?.removeSubscriber(this)
//        isRunning.value = false
    }

//    override fun received(audioFrame: AudioFrame) {
//        broadcast(
//            color = getColor(audioFrame)
//        )
//
//        printLatency()
//    }

//    private fun getColor(audioFrame: AudioFrame): Color {
//        return colorFactory.create(
//            audioFrame = audioFrame
//        )
//        return Color.Black
//    }

    private fun broadcast(color: Color) {
//        // TODO: Transition away from subscribers approach
//        subscribers.forEach {
//            it.new(color)
//        }
//
//        // TODO: Write a test that ensures that we don't crash the thread.
//        try {
//            currentGateway.value?.send(color)
//        } catch (e: Exception) {
//            Logger.error(e.message ?: "Failed to send color to gateway.")
//        }
    }

    private fun printLatency() {
//        val timeBetweenColors = System.currentTimeMillis() - timestampOfLastSentColor
//        println("Latency: $timeBetweenColors")
//        timestampOfLastSentColor = System.currentTimeMillis()
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
//        subscribers.add(subscriber)
    }

}
