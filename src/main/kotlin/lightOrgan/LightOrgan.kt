package lightOrgan

import color.ColorFactory
import input.InputSubscriber
import input.audioFrame.AudioFrame
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.bass.BassFrequencyBinListCalculator
import sound.frequencyBins.dominant.DominantFrequencyBinCalculator
import wrappers.color.Color

class LightOrgan(
    private val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
    private val colorFactory: ColorFactory = ColorFactory(),
    private val dominantFrequencyBinCalculator: DominantFrequencyBinCalculator = DominantFrequencyBinCalculator(),
    private val bassFrequencyBinListCalculator: BassFrequencyBinListCalculator = BassFrequencyBinListCalculator()
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
            frequencyBin = getDominantFrequency(audioFrame)
        )
    }

    private fun getDominantFrequency(audioFrame: AudioFrame): FrequencyBin? {
        return dominantFrequencyBinCalculator.calculate(
            frequencyBinList = getBassFrequencyBins(audioFrame)
        )
    }

    private fun getBassFrequencyBins(audioFrame: AudioFrame): FrequencyBinList {
        return bassFrequencyBinListCalculator.calculate(audioFrame)
    }

    fun checkIfSubscribed(subscriber: LightOrganSubscriber): Boolean {
        return subscribers.contains(subscriber)
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}
