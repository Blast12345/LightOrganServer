import sound.SoundService

fun main() {
    val soundService = SoundService()

    while (true) {
        val frequencyBins = soundService.getFrequencyBins()
    }
}




// start server - this isn't testable in and of itself
// start listening - this isn't testable in and of itself
// get frequency bins
// generate the RGB commands given frequency bins and LED count
// send RGB commands to client(s)