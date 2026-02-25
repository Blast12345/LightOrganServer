package dsp

import audio.samples.AudioFrame

class MonoMixer {

    fun mix(audio: AudioFrame): AudioFrame {
        if (audio.format.channels == 1) return audio

        val monoSamples = mixToMono(audio.samples, audio.format.channels)
        val monoFormat = audio.format.copy(channels = 1)

        return AudioFrame(monoSamples, monoFormat)
    }

    private fun mixToMono(samples: FloatArray, channels: Int): FloatArray {
        val monoSamples = FloatArray(samples.size / channels)

        // average all channels together
        for (i in monoSamples.indices) {
            var sum = 0f
            for (ch in 0 until channels) {
                sum += samples[i * channels + ch]
            }
            monoSamples[i] = sum / channels
        }

        return monoSamples
    }

}