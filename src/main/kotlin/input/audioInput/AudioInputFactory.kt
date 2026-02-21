package input.audioInput

import annotations.DoNotUnitTest
import input.samples.SampleBuffer
import input.samples.SampleNormalizer
import wrappers.sound.InputLine
import javax.sound.sampled.TargetDataLine

@DoNotUnitTest
class AudioInputFactory {

    fun create(
        name: String,
        dataLine: TargetDataLine
    ): AudioInput {
        val inputLine = InputLine(name, dataLine)
        val sampleNormalizer = SampleNormalizer(inputLine.bitDepth, inputLine.byteOrder)
        val oneSecondBuffer = SampleBuffer(inputLine.sampleRate * inputLine.channels)

        return AudioInput(
            inputLine = inputLine,
            sampleNormalizer = sampleNormalizer,
            sampleBuffer = oneSecondBuffer
        )
    }

}