package wrappers.audioFormat

import javax.sound.sampled.AudioFormat

class AudioFormatWrapperFactory {

    fun create(audioFormat: AudioFormat): AudioFormatWrapper {
        return AudioFormatWrapper(
            sampleRate = audioFormat.sampleRate,
            nyquistFrequency = audioFormat.sampleRate / 2,
            numberOfChannels = audioFormat.channels
        )
    }

}