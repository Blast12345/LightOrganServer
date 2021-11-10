import sound.*

fun main() {
    val lineInListener = LineInListener()
    val frequencyBinGenerator = FrequencyBinGenerator(lineInListener)

    while (true) {
        val frequencyBins = frequencyBinGenerator.getFrequencyBins()
    }
}




// start server - this isn't testable in and of itself
// start listening - this isn't testable in and of itself
// get frequency bins
// generate the RGB commands given frequency bins and LED count
// send RGB commands to client(s)