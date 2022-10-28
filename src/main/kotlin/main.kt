fun main() {
    //val lineIn = TargetLineIn()
    //val lineInListener = LineInListener(lineIn)
    //val frequencyBinGenerator = FrequencyBinGenerator(lineInListener)
    //val frequencyBins = frequencyBinGenerator.getFrequencyBins()
    // start listening - this isn't testable in and of itself
    // get frequency bins
    // generate the RGB commands given frequency bins and LED count
    // - send RGB commands to client(s)

    val lightOrgan = LightOrgan()
    lightOrgan.start()

    while (true) {
        if (!lightOrgan.isRunning) {
            lightOrgan.start()
        }
    }
}