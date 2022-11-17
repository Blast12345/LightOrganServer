package sound.frequencybins

import colorListener.sound.FrequencyBin
import sound.listener.Listener

class FrequencyBinGenerator(private val listener: Listener) {

    fun getFrequencyBins(): List<FrequencyBin> {
        val fftData = listener.getFftData()
        return convertToFrequencyBins(fftData)
    }

    private fun convertToFrequencyBins(fftData: DoubleArray): List<FrequencyBin> {
        var frequencyBins = mutableListOf<FrequencyBin>()

        fftData.forEachIndexed { index, amplitude ->
            val frequencySample = createFrequencyBin(index, amplitude)
            frequencyBins.add(frequencySample)
        }

        return frequencyBins
    }

    private fun createFrequencyBin(index: Int, amplitude: Double): FrequencyBin {
        val frequency = getFrequency(index)
        return FrequencyBin(frequency, amplitude)
    }

    private fun getFrequency(index: Int): Double {
        val sampleRate = listener.getSampleRate()
        val sampleSize = listener.getSampleSize()
        return index * sampleRate / sampleSize
    }

}