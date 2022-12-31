//package colorListener.sound.input
//
//
//interface LineInputListenerInterface {
//    fun listenForNextAudioSample(lambda: NextAudioSample)
//}
//
//// TODO: Test me
//class LineInputListener(
//    private val input: Input,
//    private val rawWaveFactory: RawWaveFactoryInterface = RawWaveFactory()
//) : LineInputListenerInterface {
//
//    private var buffer = ByteArray(input.bufferSize())
//
//    // TODO: listenForAudioSamples?
//    override fun listenForNextAudioSample(lambda: NextAudioSample) {
//        input.startListening()
//
//        while (true) {
//            // NOTE: New data becomes available every ~10 ms. I don't know how this delay is derived.
//            if (input.hasDataAvailable()) {
//                continue
//            } else {
//                // TODO: Something here isn't quite right; the frequency bins don't line up with reality. They are about 2x off.
//                // TODO: I don't think I need the sample size once I have the raw wave
//                val nextSample = getNextSample()
//                lambda(input.sampleRate().toInt(), 0, nextSample)
//            }
//        }
//    }
//
//
//}