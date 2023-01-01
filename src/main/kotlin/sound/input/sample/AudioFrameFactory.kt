package sound.input.sample

import javax.sound.sampled.TargetDataLine

interface AudioFrameFactoryInterface {
    fun audioFrameFor(samples: ByteArray, line: TargetDataLine): AudioFrame
}

// TODO: Test me
class AudioFrameFactory : AudioFrameFactoryInterface {

    override fun audioFrameFor(samples: ByteArray, line: TargetDataLine): AudioFrame {
        return AudioFrame(samples, line.format)
    }

}