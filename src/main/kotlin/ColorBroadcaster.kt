import java.awt.Color

interface ColorBroadcasterDelegate {
    fun isReadyForNextBroadcast(): Boolean
    fun getNextColor(): Color
}

interface ColorBroadcasterInterface {
    fun startBroadcasting(delegate: ColorBroadcasterDelegate)
}

class ColorBroadcaster : ColorBroadcasterInterface {
//    private val server: ServerInterface = Server(config),
//    private val systemTime: SystemTimeInterface = SystemTime()

//    private val maximumUpdatesPerSecond = 100
//    private val minimumSecondsBetweenColors = 1.0 / maximumUpdatesPerSecond
//    private var timestampOfLastSentColor = 0.0

    override fun startBroadcasting(delegate: ColorBroadcasterDelegate) {
        TODO("Not yet implemented")
    }

//    GlobalScope.launch {
//        while (isActive) {
//            broadcastColorIfNeeded()
//            delay(millisecondsUntilNextBroadcast())
//        }
//    }

//    private fun millisecondsUntilNextBroadcast(): Long {
//        val secondsSinceLastSentColor = secondsSinceLastSentColor()
//        val isReady = secondsSinceLastSentColor > minimumSecondsBetweenColors
//
//        if (isReady) {
//            return 0
//        } else {
//            return ((minimumSecondsBetweenColors - secondsSinceLastSentColor) * 1000).toLong()
//        }
//    }
//
//    private fun broadcastColorIfNeeded() {
//        val audioSignal = audioCache.get()
//
//        if (audioSignal != null && shouldSendNextColor()) {
//            broadcastColor(audioSignal)
//        }
//    }
//
//    private fun shouldSendNextColor(): Boolean {
//        return minimumSecondsBetweenColors <= secondsSinceLastSentColor()
//    }
//
//    private fun secondsSinceLastSentColor(): Double {
//        return systemTime.currentTimeInSeconds() - timestampOfLastSentColor
//    }
//
//    private fun broadcastColor(audioSignal: AudioSignal) {
//        val color = getColor(audioSignal)
//        server.sendColor(color)
//        printLatency()
//        updateTimestampOfLastSentColor()
//    }
//
//    private fun getColor(audioSignal: AudioSignal): Color {
//        return colorFactory.create(audioSignal)
//    }
//
//    private fun printLatency() {
//        val latencyInSeconds = secondsSinceLastSentColor()
//        val latencyInMilliseconds = latencyInSeconds * 1000
//        println("Latency: ${latencyInMilliseconds.toInt()}")
//    }
//
//    private fun updateTimestampOfLastSentColor() {
//        timestampOfLastSentColor = systemTime.currentTimeInSeconds()
//    }


//    private val oneSixtiethSecond = 1.0 / 60.0
//    private val twoSixtiethsSecond = oneSixtiethSecond * 2.0
//
//    @Test
//    fun `send a color to the server when an audio signal is received`() {
//        val sut = createSUT()
//        sut.receivedAudio(audioSignal)
//        verify { server.sendColor(any()) }
//    }
//
//    @Test
//    fun `limit the number of colors per second to 60 to prevent saturation`() {
//        val sut = createSUT()
//
//        every { systemTime.currentTimeInSeconds() } returns oneSixtiethSecond
//        sut.receivedAudio(audioSignal)
//        verify(exactly = 1) { server.sendColor(any()) }
//
//        sut.receivedAudio(audioSignal)
//        verify(exactly = 1) { server.sendColor(any()) }
//
//        every { systemTime.currentTimeInSeconds() } returns twoSixtiethsSecond
//        sut.receivedAudio(audioSignal)
//        verify(exactly = 2) { server.sendColor(any()) }
//    }

}