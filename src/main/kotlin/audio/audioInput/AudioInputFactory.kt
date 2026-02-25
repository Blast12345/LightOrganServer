package audio.audioInput

import annotations.SkipCoverage
import audio.samples.SampleBuffer
import audio.samples.SampleNormalizer
import wrappers.sound.InputLine
import javax.sound.sampled.TargetDataLine

@SkipCoverage
class AudioInputFactory {

    fun create(
        name: String,
        dataLine: TargetDataLine
    ): AudioInput {
        val inputLine = InputLine(name, dataLine)
        val sampleNormalizer = SampleNormalizer(inputLine.bitDepth, inputLine.byteOrder)

        val samplesPerSecond = (inputLine.sampleRate * inputLine.channels).toInt()
        val oneSecondBuffer = SampleBuffer(samplesPerSecond)

        return AudioInput(
            inputLine = inputLine,
            sampleNormalizer = sampleNormalizer,
            sampleBuffer = oneSecondBuffer
        )
    }

}