package lightOrgan

import color.ColorFactory
import input.InputSubscriber
import input.audioFrame.AudioFrame
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.dominant.DominantBassFrequencyBinCalculator
import sound.frequencyBins.listCalculator.FrequencyBinListCalculator
import wrappers.color.Color

class LightOrgan(
    private val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
    private val colorFactory: ColorFactory = ColorFactory(),
    private val frequencyBinListCalculator: FrequencyBinListCalculator = FrequencyBinListCalculator(),
    private val dominantBassFrequencyBinCalculator: DominantBassFrequencyBinCalculator = DominantBassFrequencyBinCalculator()
) : InputSubscriber {

    override fun received(audioFrame: AudioFrame) {
        broadcast(
            color = getColor(audioFrame)
        )
    }

    private fun broadcast(color: Color) {
        subscribers.forEach {
            it.new(color)
        }
    }

    private fun getColor(audioFrame: AudioFrame): Color {
        return colorFactory.create(
            frequencyBin = getDominantBassFrequencyBin(audioFrame)
        )
    }

    private fun getDominantBassFrequencyBin(audioFrame: AudioFrame): FrequencyBin? {
        return dominantBassFrequencyBinCalculator.calculate(
            frequencyBinList = getFrequencyBins(audioFrame)
        )
    }

    private fun getFrequencyBins(audioFrame: AudioFrame): FrequencyBinList {
        return frequencyBinListCalculator.calculate(audioFrame)
    }

    fun checkIfSubscribed(subscriber: LightOrganSubscriber): Boolean {
        return subscribers.contains(subscriber)
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}
